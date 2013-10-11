package com.example.android_ocr_parking;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class SendConfirmation extends AsyncTask<ConfirmRequest, Void, Boolean> {

    private String responseBody;
    private Exception exception;
    private Context applicationContext;

    public SendConfirmation(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    protected Boolean doInBackground(ConfirmRequest... params) {
        try {
            ConfirmRequest request = params[0];
            HttpClient client = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            HttpPost httpPost = new HttpPost("http://10.0.2.2:8080/hackathon_ocr/confirmParking");
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            entity.addPart("quoteId", new StringBody(String.valueOf(request.getQuoteId())));

            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost, localContext);
            responseBody = EntityUtils.toString(response.getEntity());

            return true;
        } catch (Exception e) {
            this.exception = e;
            return false;
        }
    }

    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(applicationContext, "Response:" + responseBody, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(applicationContext, "Error: " + this.exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
