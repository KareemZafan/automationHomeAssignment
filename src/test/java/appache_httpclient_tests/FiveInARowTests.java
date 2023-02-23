package appache_httpclient_tests;

import appache_httpclient_solution.FiveInARowRequests;
import com.github.javafaker.Faker;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class FiveInARowTests {


  FiveInARowRequests fiveInARowRequests = new FiveInARowRequests();
    private final String USER_REGISTRATION_URL = "/api/v1/user";
    private final String GAME_CREATION_URL = "/api/v1/connect";
    private final String MAKE_MOVE_URL = "/api/v1/play";

    @Test
    public void testUserRegistration() throws IOException {

        CloseableHttpResponse response = fiveInARowRequests.sendPostRequest( getNewUserObject().toString(), USER_REGISTRATION_URL  );

        Assert.assertEquals( response.getStatusLine().getStatusCode(), 201 );

        JSONObject jsonResponse = new JSONObject(EntityUtils.toString( response.getEntity() ));
        Assert.assertFalse( jsonResponse.get( "userId" ).toString().isEmpty() );
        Assert.assertFalse( jsonResponse.get( "userToken" ).toString().isEmpty() );

       }

  @Test
    public void testNewGameCreation() throws IOException {

        JSONObject requestBody = new JSONObject();

      CloseableHttpResponse response = fiveInARowRequests.sendPostRequest( getNewUserObject().toString(), USER_REGISTRATION_URL  );
      requestBody.put("userToken",new JSONObject(EntityUtils.toString( response.getEntity() )).get( "userToken" ));


         response = fiveInARowRequests
                .sendPostRequest( requestBody.toString(), GAME_CREATION_URL);


      Assert.assertEquals( response.getStatusLine().getStatusCode(), 201 );

      JSONObject jsonResponse = new JSONObject(EntityUtils.toString( response.getEntity() ));
      Assert.assertFalse( jsonResponse.get( "gameToken" ).toString().isEmpty() );
      Assert.assertFalse( jsonResponse.get( "gameId" ).toString().isEmpty() );


    }

    @Test
    public void testMakingMoves() throws IOException {
        JSONObject requestBody = new JSONObject();

        CloseableHttpResponse response = fiveInARowRequests.sendPostRequest( getNewUserObject().toString(), USER_REGISTRATION_URL  );
        requestBody.put("userToken",new JSONObject(EntityUtils.toString( response.getEntity() )).get( "userToken" ));

        response = fiveInARowRequests
                .sendPostRequest( requestBody.toString(), GAME_CREATION_URL);
        requestBody.put("gameToken",new JSONObject(EntityUtils.toString( response.getEntity() )).get( "gameToken" ));

        requestBody.put( "positionX", "1" );
        requestBody.put( "positionY", "0" );

         response = fiveInARowRequests.sendPostRequest( requestBody.toString(), MAKE_MOVE_URL );


        Assert.assertEquals( response.getStatusLine().getStatusCode(), 201 );

        JSONObject jsonResponse = new JSONObject(EntityUtils.toString( response.getEntity() ));
        Assert.assertFalse( jsonResponse.get( "playerCrossId" ).toString().isEmpty() );
        Assert.assertFalse( jsonResponse.get( "playerCircleId" ).toString().isEmpty() );
        Assert.assertFalse( jsonResponse.get( "actualPlayerId" ).toString().isEmpty() );
        Assert.assertFalse( jsonResponse.get( "playerCircleId" ).toString().isEmpty() );
        Assert.assertEquals( jsonResponse.get( "coordinates.x" ), "1" );
        Assert.assertEquals( jsonResponse.get( "coordinates.y" ), "0" );
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
