package apicommon;

import static io.restassured.RestAssured.given;
import common.TestLogs;
import io.restassured.response.Response;

public class ApiConfigs {

  public static String getResponse(String apiUrl, String appendQuery, String key) {
    TestLogs.info("Getting response of GET api :- '{}?{}'", apiUrl, appendQuery);
    Response response = given().when().get(apiUrl + "?" + appendQuery).then().statusCode(200)
        .extract().response();
    String value = response.path(key).toString(); // eg. key - "results.id[0]"
    TestLogs.info("Value of Key - '{}' :- '{}'", key, value);
    return value;
  }

}
