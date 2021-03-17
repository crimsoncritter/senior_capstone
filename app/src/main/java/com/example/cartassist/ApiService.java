package com.example.cartassist;

import com.example.cartassist.ApiResponseHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.HashMap;


public class ApiService {
    private String apiURI;

    public ApiService() {
        this.apiURI = "http://localhost:5000";
    }

    public String login(String email, String password) throws IOException {
        String body = new ObjectMapper().writeValueAsString(
                new HashMap<String, String>() {{
            put("name", email);
            put ("password", password);
        }});

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(apiURI + "/user/auth/init");
        StringEntity entity = new StringEntity(body);
        request.setEntity(entity);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        try {
            String token = (String) client.execute(request, new ApiResponseHandler());
            client.close();

            return token;
        } catch(IOException ex) {
            throw ex;
        }
    }
}
