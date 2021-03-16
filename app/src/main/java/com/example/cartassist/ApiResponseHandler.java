package com.example.cartassist;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ApiResponseHandler implements ResponseHandler {
    public Object handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();

        if (status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? EntityUtils.toString(entity) : null;
        } else if (status == 401) {
            return "Invalid";
        }
        else {
            throw new ClientProtocolException("Unable to complete request:" + status);
        }
    }
}
