package base;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class BaseClass {
    public static Properties prop;
    public static String token;

    static {
            prop= new Properties();
            try (FileInputStream fis = new FileInputStream( System.getProperty("user.dir")
                    + "\\src\\main\\resources\\config.properties")) {
                prop.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static String getToken() {
        if(token == null) {

            Response response = RestAssured.given()
                    .baseUri(prop.getProperty("baseURL"))
                    .basePath(prop.getProperty("getTokenPath"))
                    .contentType(ContentType.URLENC)
                    .formParam("grant_type", prop.getProperty("grant_type"))
                    .formParam("client_id", prop.getProperty("client_id"))
                    .formParam("client_secret", prop.getProperty("client_secret"))
                    .post().then().log().all().extract().response();

            String responseBody = response.getBody().asString();
            JsonPath jsonPath = new JsonPath(responseBody);
            token = jsonPath.getString("access_token");
        }
        return token;
    }

    public JsonPath getJsonPath(Response response) {
        String responseBody = response.getBody().asString();
        return new JsonPath(responseBody);
    }
}
