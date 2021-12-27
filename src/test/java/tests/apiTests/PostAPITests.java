package tests.apiTests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.baseTests.BaseAPITest;
import utils.CommonUtils;
import utils.RestUtils;

public class PostAPITests extends BaseAPITest {

    @Test(groups = {"postServices", "regression", "apiTests"})
    public void tc_p1_validCreateUser_test() {
        String createUserRequest = CommonUtils.readTemplate("createUser");
        String createUserEndpoint = CommonUtils.getEndpoint("createUser");
        Response response = RestUtils.postRequestAndReturnResponse(createUserEndpoint, createUserRequest);
        Assert.assertEquals(response.statusCode(), 201,
                "Asserting for valid status code on user creation");
        Assert.assertTrue(response.jsonPath().getInt("id") > 0,
                "Asserting if valid ID is generated");
    }

    @Test(groups = {"postServices", "regression", "apiTests"})
    public void tc_p2_validCreateUserInvalidFormat_test() {
        String createUserRequest = CommonUtils.readTemplate("createUserInvalid");
        String createUserEndpoint = CommonUtils.getEndpoint("createUser");
        Response response = RestUtils.postRequestAndReturnResponse(
                                    createUserEndpoint,
                                    createUserRequest,
                                    ContentType.XML);
        testAssertion.assertEquals(response.statusCode(), 415,
                "Asserting for valid status code on user creation");
        testAssertion.assertEquals(response.statusLine(), "HTTP/1.1 415 Unsupported Media Type",
                "Asserting for valid status code on user creation");
    }

    @Test(groups = {"postServices", "regression", "apiTests"})
    public void tc_p2_validCreateUser_Auth_test() {
        String createUserRequest = "{\"name\":\"Tenali Ramakrishna\", \"gender\":\"male\", \"email\":\"tenali.ramakrishna@15ce.com\", \"status\":\"active\"}";
        String createUserEndpoint = "https://gorest.co.in/public/v1/users";
        // Only free method with token available - Hardcoding for demo purposes
        String bearerToken = "730cc0725e6baef7c0b890c4e40eb38ac204732062dc8eb5a6224a6b2560d9cc";
        Response response = RestUtils.postRequestAndReturnResponse(
                                    createUserEndpoint,
                                    bearerToken,
                                    createUserRequest,
                                    ContentType.XML);
        testAssertion.assertEquals(response.statusCode(), 201,
                "Asserting for valid status code on user creation");
    }

    @Test(groups = {"postServices", "regression", "apiTests"})
    public void tc_p2_validCreateUser_invalidAuth_test() {
        String createUserRequest = "{\"name\":\"Tenali Ramakrishna\", \"gender\":\"male\", \"email\":\"tenali.ramakrishna@15ce.com\", \"status\":\"active\"}";
        String createUserEndpoint = "https://gorest.co.in/public/v1/users";
        // Only free method with token available - Hardcoding for demo purposes
        String bearerToken = "730cc0725e6baef7c0b890c4e40eb38ac204732062dc8eb5a6224a6b2560d9ccAA";
        Response response = RestUtils.postRequestAndReturnResponse(
                createUserEndpoint,
                bearerToken,
                createUserRequest,
                ContentType.XML);
        testAssertion.assertEquals(response.statusCode(), 401,
                "Asserting for valid status code on user creation");
    }
}
