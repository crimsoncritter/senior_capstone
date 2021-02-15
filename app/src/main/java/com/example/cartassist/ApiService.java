package com.example.cartassist;

import java.io.Closeable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.HashMap;


public class ApiService {
    private String apiURI;

    public ApiService() {
        this.apiURI = "http://localhost:5000";
    }

    public String login(String email, String password) {
        String body = new ObjectMapper().writeValuesAsString(
                new HashMap<String, String>() {{
            put("name", email);
            put ("occupation", password);
        }});

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = client.newBuilder()
                .uri(URI.create(apiURI + "/user/auth/init"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
