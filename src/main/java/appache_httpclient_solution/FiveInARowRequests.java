package appache_httpclient_solution;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FiveInARowRequests {
    private static final String BASE_URL = "https://piskvorky.jobs.cz";

    public CloseableHttpResponse sendPostRequest(String requestBody, String url) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(BASE_URL + url);
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity( requestBody));

        return httpClient.execute( request );

    }
}
