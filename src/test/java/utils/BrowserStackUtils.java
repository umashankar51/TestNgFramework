package utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Retrieves session video evidence from BrowserStack App Automate.
 *
 * Video processing on BrowserStack takes time after session ends.
 * {@link #getVideoUrl} retries for up to ~2 minutes before giving up.
 */
@Slf4j
public class BrowserStackUtils {

    private static final String APP_AUTOMATE_API = "https://api-cloud.browserstack.com/app-automate";
    private static final int    MAX_RETRIES      = 12;   // 12 × 10 s = 2 min
    private static final int    RETRY_DELAY_MS   = 10_000;

    /**
     * Polls BrowserStack until the video URL is available for {@code sessionId}.
     *
     * @return signed S3 video URL, or {@code null} if not available within timeout
     */
    public static String getVideoUrl(String sessionId) {
        String username  = ConfigUtils.getTestVariable("bs_username");
        String accessKey = ConfigUtils.getTestVariable("bs_accessKey");

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                Response response = RestAssured.given()
                        .auth().basic(username, accessKey)
                        .get(APP_AUTOMATE_API + "/sessions/" + sessionId + ".json");

                if (response.getStatusCode() == 200) {
                    String videoUrl = response.jsonPath()
                            .getString("automation_session.video_url");
                    if (videoUrl != null && !videoUrl.isBlank()) {
                        log.info("BrowserStack video ready for session {}: {}", sessionId, videoUrl);
                        return videoUrl;
                    }
                }
                log.info("Video not ready yet for session {} (attempt {}/{}), waiting {}s…",
                        sessionId, attempt, MAX_RETRIES, RETRY_DELAY_MS / 1000);
                Thread.sleep(RETRY_DELAY_MS);

            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.warn("Error fetching BrowserStack session {}: {}", sessionId, e.getMessage());
            }
        }
        log.warn("BrowserStack video not available for session {} after {} attempts", sessionId, MAX_RETRIES);
        return null;
    }

    /**
     * Downloads the video at {@code videoUrl} to a temp MP4 file.
     *
     * @return {@link File} handle, or {@code null} on failure
     */
    public static File downloadVideo(String sessionId, String videoUrl) {
        try {
            File videoFile = File.createTempFile("bs_" + sessionId + "_", ".mp4");
            videoFile.deleteOnExit();

            try (InputStream in  = new URL(videoUrl).openStream();
                 FileOutputStream out = new FileOutputStream(videoFile)) {
                byte[] buf = new byte[16_384];
                int read;
                while ((read = in.read(buf)) != -1) {
                    out.write(buf, 0, read);
                }
            }
            log.info("Downloaded BrowserStack video ({} bytes) → {}", videoFile.length(), videoFile.getAbsolutePath());
            return videoFile;

        } catch (Exception e) {
            log.error("Failed to download BrowserStack video for session {}: {}", sessionId, e.getMessage());
            return null;
        }
    }
}