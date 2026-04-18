package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;

import java.util.Map;

@Slf4j
public class RestUtils {

    public static Response getRequestResponse(String path) {
        log.info("request path : {}", path);
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .get(path);
    }

    public static String getRequestResponse(String path, int expectedResponseCode) {
        log.info("request path : {}", path);
        Response response = RestAssured.given().contentType(ContentType.JSON).get(path);
        if (response != null && (response.getStatusCode() == expectedResponseCode)) {
            return response.asString();
        } else {
            Assert.assertTrue(false, "Asserting if Response Status code is " + expectedResponseCode);
            return null;
        }
    }

    public static Response getRequestResponse(String path,
                                              Map<String, String> queryParams,
                                              Map<String, String> pathParams) {
        log.info("request path : {}", path);
        return RestAssured.given().contentType(ContentType.JSON)
                .pathParams(pathParams)
                .queryParams(queryParams)
                .get(path);
    }

    public static Response getRequestResponseByQueryParams(String path, Map<String, String> queryParams) {
        log.info("request path : {}", path);
        return RestAssured.given().contentType(ContentType.JSON)
                .queryParams(queryParams)
                .get(path);
    }

    public static Response getRequestResponseByPathParams(String path, Map<String, String> pathParams) {
        log.info("request path : {}", path);
        return RestAssured.given().contentType(ContentType.JSON)
                .pathParams(pathParams)
                .get(path);
    }

    public static String postRequestResponse(String path,
                                             String body,
                                             Map<String, String> headers,
                                             int expectedStatusCode) {
        log.info("Request Path : {}", path);
        log.info("Request Json : {}", body);
        log.info("Request Headers : {}", headers);
        Response response = null;
        try {
            if (body != null) {
                response = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .headers(headers)
                        .body(body)
                        .post(path);
                Assert.assertTrue((response.getStatusCode() == expectedStatusCode),
                        "Status Code for response : " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }
        log.info("response.asString(): {}", response.asString());
        return response.asString();
    }

    public static Response postRequestAndReturnResponse(String path, String body) {
        log.info("Request Path : {}", path);
        log.info("Request Json : {}", body);
        Response response = null;
        try {
            if (body != null) {
                response = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(body)
                        .post(path);
            }
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }
        return response;
    }

    public static Response postRequestAndReturnResponse(String path, String body, ContentType contentType) {
        log.info("Request Path : {}", path);
        log.info("Request Json : {}", body);
        Response response = null;
        try {
            if (body != null) {
                response = RestAssured.given()
                        .contentType(contentType)
                        .body(body)
                        .post(path);
            }
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }
        return response;
    }

    public static Response postRequestAndReturnResponse(String path, String bearerToken, String body, ContentType contentType) {
        log.info("Request Path : {}", path);
        log.info("Request Json : {}", body);
        Response response = null;
        try {
            if (body != null) {
                response = RestAssured.given()
                        .header(new Header("Authorization", "Bearer " + bearerToken))
                        .contentType(contentType)
                        .body(body)
                        .post(path);
            }
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
        }
        return response;
    }
}
