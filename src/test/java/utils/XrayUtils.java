package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Xray Cloud REST API v2 integration.
 *
 * Execution key resolution order (first non-blank wins):
 *   1. Azure env var  XRAY_EXECUTION_KEY
 *   2. test.properties key  xray_execution_key
 *   3. Create a new execution if  XRAY_CREATE_NEW=true  (or xray_create_new=true in properties)
 *   4. Skip — no Xray reporting
 *
 * Authenticate once in {@link #init(String)}, then call
 * {@link #reportTestResult(String, String, File)} per test after the driver session ends.
 */
@Slf4j
public class XrayUtils {

    private static final String XRAY_BASE = "https://xray.cloud.getxray.app/api/v2";

    private static final AtomicReference<String> TOKEN        = new AtomicReference<>();
    private static final AtomicReference<String> EXEC_KEY     = new AtomicReference<>();

    // ── init ──────────────────────────────────────────────────────────────────

    /**
     * Must be called once (from {@code @BeforeSuite}) before any test runs.
     *
     * @param buildId Azure Build ID — used in the auto-created execution summary
     */
    public static synchronized void init(String buildId) {
        authenticate();
        if (TOKEN.get() == null) return;          // auth failed — skip everything
        resolveExecutionKey(buildId);
    }

    private static void authenticate() {
        String clientId     = resolve("XRAY_CLIENT_ID",     "xray_client_id");
        String clientSecret = resolve("XRAY_CLIENT_SECRET", "xray_client_secret");

        if (clientId == null || clientSecret == null) {
            log.warn("Xray credentials not configured (XRAY_CLIENT_ID / XRAY_CLIENT_SECRET) — Xray reporting disabled");
            return;
        }

        String body = "{\"client_id\":\"" + clientId + "\",\"client_secret\":\"" + clientSecret + "\"}";
        try {
            Response resp = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(body)
                    .post(XRAY_BASE + "/authenticate");

            if (resp.getStatusCode() == 200) {
                TOKEN.set(resp.asString().replace("\"", "").trim());
                log.info("Xray Cloud authentication successful");
            } else {
                log.warn("Xray authentication failed: HTTP {} — {}", resp.getStatusCode(), resp.getBody().asString());
            }
        } catch (Exception e) {
            log.error("Xray authentication error: {}", e.getMessage());
        }
    }

    private static void resolveExecutionKey(String buildId) {
        // 1. Azure env var
        String key = resolve("XRAY_EXECUTION_KEY", "xray_execution_key");
        if (key != null && !key.isBlank()) {
            EXEC_KEY.set(key);
            log.info("Using Xray execution key: {}", key);
            return;
        }

        // 2. createNew flag
        String createNew = resolve("XRAY_CREATE_NEW", "xray_create_new");
        if ("true".equalsIgnoreCase(createNew)) {
            String newKey = createTestExecution(buildId);
            if (newKey != null) {
                EXEC_KEY.set(newKey);
                log.info("Created new Xray test execution: {}", newKey);
            }
        } else {
            log.info("No XRAY_EXECUTION_KEY provided and XRAY_CREATE_NEW is not true — Xray reporting skipped");
        }
    }

    // ── execution management ──────────────────────────────────────────────────

    private static String createTestExecution(String buildId) {
        String jiraUrl    = resolve("JIRA_BASE_URL",   "jira_base_url");
        String email      = resolve("JIRA_EMAIL",      "jira_email");
        String apiToken   = resolve("JIRA_API_TOKEN",  "jira_api_token");
        String projectKey = resolve("XRAY_PROJECT_KEY","xray_project_key");

        if (jiraUrl == null || email == null || apiToken == null || projectKey == null) {
            log.warn("Jira config incomplete — cannot create test execution");
            return null;
        }

        String summary = "Automated Test Execution — Build " + (buildId != null ? buildId : "N/A");
        String body = "{"
                + "\"fields\":{"
                + "\"project\":{\"key\":\"" + projectKey + "\"},"
                + "\"summary\":\"" + summary + "\","
                + "\"issuetype\":{\"name\":\"Test Execution\"}"
                + "}}";

        try {
            Response resp = RestAssured.given()
                    .auth().preemptive().basic(email, apiToken)
                    .contentType(ContentType.JSON)
                    .body(body)
                    .post(jiraUrl + "/rest/api/2/issue");

            if (resp.getStatusCode() == 201) {
                return resp.jsonPath().getString("key");
            }
            log.warn("Failed to create Test Execution: HTTP {} — {}", resp.getStatusCode(), resp.getBody().asString());
        } catch (Exception e) {
            log.error("Error creating Xray Test Execution: {}", e.getMessage());
        }
        return null;
    }

    // ── per-test reporting ────────────────────────────────────────────────────

    /**
     * Reports a single test result to Xray.
     *
     * @param jiraKey  Xray test issue key (from {@code @Test(description)})
     * @param status   "PASSED", "FAILED", or "SKIPPED"
     * @param evidence BrowserStack MP4 file (may be {@code null} — evidence step is skipped)
     */
    public static void reportTestResult(String jiraKey, String status, File evidence) {
        if (!isEnabled()) return;
        if (jiraKey == null || jiraKey.isBlank() || jiraKey.equals("-")) {
            log.debug("No Jira key for test — skipping Xray update");
            return;
        }

        String token   = TOKEN.get();
        String execKey = EXEC_KEY.get();

        // 1. Add test to execution (idempotent)
        addTestToExecution(execKey, jiraKey, token);

        // 2. Get test run ID
        Integer testRunId = getTestRunId(execKey, jiraKey, token);
        if (testRunId == null) {
            log.warn("Could not find test run for {} in {} — skipping update", jiraKey, execKey);
            return;
        }

        // 3. Upload BrowserStack recording as evidence
        if (evidence != null && evidence.exists()) {
            uploadEvidence(testRunId, evidence, token);
        }

        // 4. Update status
        updateStatus(testRunId, status, token);
    }

    // ── Xray API calls ────────────────────────────────────────────────────────

    private static void addTestToExecution(String execKey, String testKey, String token) {
        try {
            String body = "{\"add\":[\"" + testKey + "\"]}";
            Response resp = RestAssured.given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .body(body)
                    .post(XRAY_BASE + "/testexec/" + execKey + "/tests");

            log.info("Add {} to execution {}: HTTP {}", testKey, execKey, resp.getStatusCode());
        } catch (Exception e) {
            log.error("Error adding test to execution: {}", e.getMessage());
        }
    }

    private static Integer getTestRunId(String execKey, String testKey, String token) {
        try {
            Response resp = RestAssured.given()
                    .header("Authorization", "Bearer " + token)
                    .queryParam("testIssueKey", testKey)
                    .get(XRAY_BASE + "/testexec/" + execKey + "/testruns");

            if (resp.getStatusCode() == 200) {
                List<Integer> ids = resp.jsonPath().getList("id", Integer.class);
                if (ids != null && !ids.isEmpty()) return ids.get(0);
            }
            log.warn("Get test run for {} in {}: HTTP {} — {}", testKey, execKey,
                    resp.getStatusCode(), resp.getBody().asString());
        } catch (Exception e) {
            log.error("Error fetching test run ID: {}", e.getMessage());
        }
        return null;
    }

    private static void uploadEvidence(int testRunId, File file, String token) {
        try {
            Response resp = RestAssured.given()
                    .header("Authorization", "Bearer " + token)
                    .multiPart("file", file, "video/mp4")
                    .post(XRAY_BASE + "/testrun/" + testRunId + "/evidence");

            log.info("Upload evidence to test run {}: HTTP {}", testRunId, resp.getStatusCode());
        } catch (Exception e) {
            log.error("Error uploading evidence: {}", e.getMessage());
        }
    }

    private static void updateStatus(int testRunId, String status, String token) {
        try {
            Response resp = RestAssured.given()
                    .header("Authorization", "Bearer " + token)
                    .contentType(ContentType.JSON)
                    .body("\"" + status + "\"")
                    .put(XRAY_BASE + "/testrun/" + testRunId + "/status");

            log.info("Updated test run {} → {}: HTTP {}", testRunId, status, resp.getStatusCode());
        } catch (Exception e) {
            log.error("Error updating test run status: {}", e.getMessage());
        }
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    public static boolean isEnabled() {
        return TOKEN.get() != null && EXEC_KEY.get() != null;
    }

    /**
     * Checks env var first (Azure), then falls back to test.properties.
     */
    private static String resolve(String envKey, String propKey) {
        String val = System.getenv(envKey);
        if (val != null && !val.isBlank()) return val;
        return ConfigUtils.getTestVariable(propKey);
    }
}
