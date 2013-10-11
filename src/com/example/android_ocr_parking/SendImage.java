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
import java.io.File;

public class SendImage extends AsyncTask<ParkingRequest, Void, Boolean> {

    private Exception exception;
    private Context applicationContext;
    private String responseBody;

    public SendImage(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected Boolean doInBackground(ParkingRequest... receipts) {
        try {
            ParkingRequest receipt = receipts[0];
            HttpClient client = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            File imageFile = new File(receipt.GetImagePath());
//            File imageFile = new File("/img1.jpg");

            HttpPost httpPost = new HttpPost("http://10.0.2.2:8080/hackathon_ocr/requestParkingQuote");
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            entity.addPart("userId", new StringBody(String.valueOf(receipt.GetUserId())));
            entity.addPart("pin", new StringBody(String.valueOf(receipt.GetPin())));
            entity.addPart("duration", new StringBody(String.valueOf(receipt.GetDuration())));
            entity.addPart("file", new FileBody(imageFile));

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
        // TODO: check this.exception
        // TODO: do something with the feed

        if (result) {
            //Toast.makeText(applicationContext, "sent", Toast.LENGTH_SHORT).show();
            Toast.makeText(applicationContext, "response:" + responseBody, Toast.LENGTH_LONG).show();

            //show confirmation screen
            Intent confirmIntent = new Intent(applicationContext, ConfirmActivity.class);
            confirmIntent.putExtra("response", responseBody);
            confirmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            applicationContext.startActivity(confirmIntent);
        } else {
            Toast.makeText(applicationContext, "Error: " + this.exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
