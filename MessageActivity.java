package com.bywalamedia.hub.pelicantablocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class MessageActivity extends AppCompatActivity {

    public static final String TAG = MessageActivity.class.getSimpleName();

    String currentLongitude;
    String currentLatitude;
    String timestamp;
    String comment;

    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        Bundle locateBundle = intent.getExtras();
        currentLongitude = locateBundle.getString("LONGITUDE");
        currentLatitude = locateBundle.getString("LATITUDE");
        timestamp = locateBundle.getString("TIMESTAMP");

        responseText = findViewById(R.id.responseText);
        TextView textViewTime = findViewById(R.id.textViewTime);
        TextView textViewLon = findViewById(R.id.textViewLon);
        TextView textViewLat = findViewById(R.id.textViewLat);

        textViewLat.setText(currentLatitude);
        textViewLon.setText(currentLongitude);
        textViewTime.setText(timestamp);
        responseText.setText("Ready to transmit...\n");
    }

    public void sendMessage(View view) {

        responseText = findViewById(R.id.responseText);

        final EditText userComment = findViewById(R.id.userComment);
        comment = userComment.getText().toString();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://1612.duckdns.org/datasources/pelicanTabLocationInsert.php";
        responseText.append("Transmitting to "+url+" \n");

        StringRequest request = new StringRequest(StringRequest.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(MapsActivity.this, "dfdsfsd" + response, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Response!");
                responseText.append("Recieved the following response: \n"+response+"\n");
                responseText.append("Activity complete, return to map screen...\n");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(this, "my error :"+error, Toast.LENGTH_LONG).show();
                Log.i(TAG, "Error!");
                responseText.append("Recieved the following response: \n"+error+"\n");
                responseText.append("Activity complete, return to map screen...\n");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                //Map longitude and latitude for POST request
                map.put("longitude", currentLongitude);
                map.put("latitude", currentLatitude);
                map.put("timestamp", timestamp);
                map.put("comment", comment);
                return map;
            }
        };

        queue.add(request);
        Button button = findViewById(R.id.button2);
        button.setEnabled(false);

    }
}
