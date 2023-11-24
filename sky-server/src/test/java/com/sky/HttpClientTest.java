package com.sky;

import com.google.gson.JsonObject;
import net.minidev.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

public class HttpClientTest {

    @Test
    public void testGet() throws  Exception {
//        create httpclient object
        CloseableHttpClient httpClient = HttpClients.createDefault();

//        create get request object
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");

//        send request
        CloseableHttpResponse response = httpClient.execute(httpGet);

//        get response code
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Server status code: " + statusCode);

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println("data server returns: " + body);

//        close resources
        response.close();
        httpClient.close();
    }

    @Test
    public void testPost() throws Exception {
//        create post httpclient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");

//        create a json object and ready for sending
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "admin");
        jsonObject.put("password", "123456");

        StringEntity entity = new StringEntity(jsonObject.toString());  // turn jsonObject to string type

        entity.setContentEncoding("utf-8");  // encode way
        entity.setContentType("application/json");   // fixed contentType

        httpPost.setEntity(entity);  // assign post entity to httpPost

//        create post request object
        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Server status code: " + statusCode);

        HttpEntity entity1 = response.getEntity();
        String body = EntityUtils.toString(entity1);
        System.out.println("data server returns: " + body);

        response.close();
        httpClient.close();
    }
}
