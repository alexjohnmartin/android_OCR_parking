package com.example.android_ocr_parking;

import android.content.Context;
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
import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class SendImage extends AsyncTask<ParkingRequest, Void, Boolean> {

    private Exception exception;
    private Context applicationContext;

    public SendImage(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected Boolean doInBackground(ParkingRequest... receipts) {
        try {
            ParkingRequest receipt = receipts[0];
            HttpClient client = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            File imageFile = new File(receipt.GetImagePath());

            HttpPost httpPost = new HttpPost("http://10.0.2.2:8080/hackathon_ocr/uploadImage");
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("userId", new StringBody(String.valueOf(receipt.GetUserId())));
//            entity.addPart("total", new StringBody(String.valueOf(receipt.GetTotal())));
//            Date today = new Date();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(today);
//            String date_string = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DATE);
//            entity.addPart("category", new StringBody(receipt.GetCategory()));
//            entity.addPart("date", new StringBody(date_string));
            entity.addPart("file", new FileBody(imageFile));
            httpPost.setEntity(entity);
            client.execute(httpPost, localContext);

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
            Toast.makeText(applicationContext, "sent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(applicationContext, "Error: " + this.exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
