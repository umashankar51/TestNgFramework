package utils;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.util.Map;


public class RestUtils {

    /**************** logger **************/
    private static final Logger LOGGER = LogManager.getLogger(RestUtils.class);

    public static Response getRequestResponse(String path) {
        LOGGER.info("request path : " + path);
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .get(path);
        return response;
    }

    public static String getRequestResponse(String path,
                                            int expectedResponseCode) {
        LOGGER.info("request path : " + path);
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .get(path);
        if (response != null && (response.getStatusCode() == expectedResponseCode)) {
            return response.asString();
        } else {
            Assert.assertTrue(false,
                    "Asserting if Response Status code is "+expectedResponseCode);
            return null;
        }
    }

    public static Response getRequestResponse(String path,
                                            Map<String, String> queryParams,
                                            Map<String, String> pathParams) {
        LOGGER.info("request path : " + path);
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .pathParams(pathParams)
                .queryParams(queryParams)
                .get(path);

        if (response != null) {
            return response;
        } else {
            return null;
        }
    }

    public static Response getRequestResponseByQueryParams(String path,
                                              Map<String, String> queryParams) {
        LOGGER.info("request path : " + path);
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .queryParams(queryParams)
                .get(path);

        if (response != null) {
            return response;
        } else {
            return null;
        }
    }

    public static Response getRequestResponseByPathParams(String path,
                                              Map<String, String> pathParams) {
        LOGGER.info("request path : " + path);
        Response response = RestAssured.given().contentType(ContentType.JSON)
                .pathParams(pathParams)
                .get(path);

        if (response != null) {
            return response;
        } else {
            return null;
        }
    }

    public static String postRequestResponse(String path,
                                             String body,
                                             Map<String, String> headers,
                                             int expectedStatusCode) {
        LOGGER.info("Request Path : " + path);
        LOGGER.info("Request Json : " + body);
        LOGGER.info("Request Headers : " + headers);

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
            LOGGER.info(e.getLocalizedMessage());
        }
        LOGGER.info("response.asString(): " + response.asString());
        return response.asString();
    }

    public static Response postRequestAndReturnResponse(String path, String body) {
        LOGGER.info("Request Path : " + path);
        LOGGER.info("Request Json : " + body);
        Response response = null;
        try {
            if (body != null) {
                response = RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(body)
                        .post(path);
            }
        } catch (Exception e) {
            LOGGER.info(e.getLocalizedMessage());
        }
        return response;
    }

    public static Response postRequestAndReturnResponse(String path, String body, ContentType contentType) {
        LOGGER.info("Request Path : " + path);
        LOGGER.info("Request Json : " + body);
        Response response = null;
        try {
            if (body != null) {
                response = RestAssured.given()
                        .contentType(contentType)
                        .body(body)
                        .post(path);
            }
        } catch (Exception e) {
            LOGGER.info(e.getLocalizedMessage());
        }
        return response;
    }

    public static Response postRequestAndReturnResponse(String path, String bearerToken, String body, ContentType contentType) {
        LOGGER.info("Request Path : " + path);
        LOGGER.info("Request Json : " + body);
        Response response = null;
        try {
            if (body != null) {
                response = RestAssured.given()
                        .header(new Header("Authorization","Bearer "+bearerToken))
                        .contentType(contentType)
                        .body(body)
                        .post(path);
            }
        } catch (Exception e) {
            LOGGER.info(e.getLocalizedMessage());
        }
        return response;
    }
}