package restassured_tests;

import com.github.javafaker.Faker;
import restassured_solution.FiveInARowRequests;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FiveInARowTests {

    FiveInARowRequests fiveInARowRequests = new FiveInARowRequests();
    private final String USER_REGISTRATION_URL = "/api/v1/user";
    private final String GAME_CREATION_URL = "/api/v1/connect";
    private final String MAKE_MOVE_URL = "/api/v1/play";


    @Test()
    public void testUserRegistration() {

        Response response = fiveInARowRequests.sendPostRequest( getNewUserObject().toString(), USER_REGISTRATION_URL  );

        Assert.assertEquals( response.statusCode(), 201 );
        Assert.assertFalse( response.jsonPath().get( "userId" ).toString().isEmpty() );
        Assert.assertFalse( response.jsonPath().get( "userToken" ).toString().isEmpty() );
    }

    @Test
    public void testNewGameCreation() {

        JSONObject requestBody = new JSONObject();
        String userToken =
                fiveInARowRequests
                        .sendPostRequest( getNewUserObject().toString(), USER_REGISTRATION_URL )
                        .jsonPath()
                        .get( "userToken" ).toString();

        requestBody.put( "userToken", userToken );

        Response response = fiveInARowRequests
                .sendPostRequest( requestBody.toString(), GAME_CREATION_URL);


        Assert.assertEquals( response.statusCode(), 201 );
        Assert.assertFalse( response.jsonPath().get( "gameToken" ).toString().isEmpty() );
        Assert.assertFalse( response.jsonPath().get( "gameId" ).toString().isEmpty() );


    }

    @Test
    public void testMakingMoves() {
        JSONObject requestBody = new JSONObject();
        String userToken =
                fiveInARowRequests
                        .sendPostRequest( getNewUserObject().toString(), USER_REGISTRATION_URL )
                        .jsonPath()
                        .get( "userToken" ).toString();
        requestBody.put( "userToken", userToken );

        String gameToken = fiveInARowRequests
                .sendPostRequest( requestBody.toString(), GAME_CREATION_URL).jsonPath().get( "gameToken" ).toString();
        requestBody.put( "gameToken", gameToken );
        requestBody.put( "positionX", "1" );
        requestBody.put( "positionY", "0" );

        Response response = fiveInARowRequests.sendPostRequest( requestBody.toString(), MAKE_MOVE_URL );

        Assert.assertEquals( response.statusCode(), 201 );
        Assert.assertFalse( response.jsonPath().get( "playerCrossId" ).toString().isEmpty() );
        Assert.assertFalse( response.jsonPath().get( "playerCircleId" ).toString().isEmpty() );
        Assert.assertFalse( response.jsonPath().get( "actualPlayerId" ).toString().isEmpty() );
        Assert.assertEquals( response.jsonPath().get( "coordinates.x" ), "1" );
        Assert.assertEquals( response.jsonPath().get( "coordinates.y" ), "0" );
    }

    /**
     * @return JSONObject with the generated nickname and email to register a user
     */
    private JSONObject getNewUserObject() {

        //Using Faker library to generate random username and email for registration
        Faker faker = new Faker();
        String userName = faker.name().username();
        String email = faker.internet().emailAddress();
        JSONObject requestBody = new JSONObject();
        requestBody.put( "nickname", userName );
        requestBody.put( "email", email );

        return requestBody;
    }
}
