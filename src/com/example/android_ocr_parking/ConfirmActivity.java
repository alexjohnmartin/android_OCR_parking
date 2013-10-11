package com.example.android_ocr_parking;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmActivity extends Activity {

    private TextView textView;
    private double price;
    private String quoteId;
    private String locationName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm);
        textView = (TextView)findViewById(R.id.text_view);

        String response = getIntent().getStringExtra("response");

        //parse JSON response
        try {
            JSONObject responseJson = new JSONObject(response);
            quoteId = responseJson.getString("QuoteId");
            price = responseJson.getDouble("Price");
            locationName = responseJson.getString("LocationName");
            textView.setText("Quote:" + quoteId + ", price:" + price + ", location:" + locationName);
        } catch (JSONException e) {
            Toast.makeText(this.getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void onCancelClick(View view) {
        finish();
    }

    public void onConfirmClick(View view) {
        ConfirmRequest request = new ConfirmRequest();
        request.setQuoteId(quoteId);
        new SendConfirmation(getApplicationContext()).execute(request);
        finish();
    }
}
