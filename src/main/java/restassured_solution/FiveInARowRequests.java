package restassured_solution;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class FiveInARowRequests {

    private static final String BASE_URL = "https://piskvorky.jobs.cz";

    public Response sendPostRequest(String requestBody, String url) {

        return given()
                .contentType( ContentType.JSON )
                .and()
                .body( requestBody )
                .when()
                .post( BASE_URL + url )
                .then()
                .log()
                .all()
                .extract()
                .response();
    }
}
