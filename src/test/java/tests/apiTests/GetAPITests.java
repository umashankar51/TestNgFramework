package tests.apiTests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.baseTests.BaseAPITest;
import utils.CommonUtils;
import utils.RestUtils;

import java.util.HashMap;
import java.util.Map;

public class GetAPITests extends BaseAPITest {

    @Test(groups= {"getServices","regression","apiTests"})
    public void tc_p1_validGetUserByID_test()  {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("userID", "2");
        String getEndPoint = CommonUtils.getEndpoint("getUserByID");
        Response response = RestUtils.getRequestResponseByPathParams(getEndPoint, pathParams);
        Assert.assertEquals(response.statusCode(), 200,
                "Asserting for valid status code on user retrieval");
    }

    @Test(groups= {"getServices","regression","apiTests"})
    public void tc_p1_validGetUserByPage_test()  {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", "2");
        String getEndPoint = CommonUtils.getEndpoint("getUsersList");
        Response response = RestUtils.getRequestResponseByQueryParams(getEndPoint, queryParams);
        Assert.assertEquals(response.statusCode(), 200,
                "Asserting for valid status code on user retrieval");
        JsonPath jsonResponse = new JsonPath(response.asString());
        testAssertion.assertEquals(jsonResponse.get("data.find {data -> data.id == 7}.first_name"),
                "Michael",
                "Asserting if user first name is correct");
        testAssertion.assertEquals(jsonResponse.get("data.find {data -> data.id == 7}.last_name"),
                "Lawson",
                "Asserting if user last name is correct");
        testAssertion.assertEquals(jsonResponse.get("data.find {data -> data.id == 7}.email"),
                "michael.lawson@reqres.com",
                "Asserting if user email is correct");
        testAssertion.assertAll();
    }

    @Test(groups= {"getServices","regression","apiTests"})
    public void tc_p2_validGetUserByID_userNotFound()  {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("userID", "23");
        String getEndPoint = CommonUtils.getEndpoint("getUserByID");
        Response response = RestUtils.getRequestResponseByPathParams(getEndPoint, pathParams);
        Assert.assertEquals(response.statusCode(), 404,
                "Asserting for valid status code displayed when user not retrieved");
    }

}
