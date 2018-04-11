package edu.illinois.cs.cs125.lab11;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Main screen for our API testing app.
 */
public final class MainActivity extends AppCompatActivity {
    /**
     * Default logging tag for messages from the main activity.
     */
    private static final String TAG = "Lab11:Main";

    /**
     * Request queue for our network requests.
     */
    private static RequestQueue requestQueue;

    /**
     * Run when our activity comes into view.
     *
     * @param savedInstanceState state that was saved by the activity last time it was paused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up a queue for our Volley requests
        requestQueue = Volley.newRequestQueue(this);

        // Load the main layout for our activity
        setContentView(R.layout.activity_main);

        // Attach the handler to our UI button
        final Button startAPICall = findViewById(R.id.startAPICall);
        startAPICall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Start API button clicked");
                startAPICall();
            }
        });

        // Make sure that our progress bar isn't spinning and style it a bit
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * Make an API call.
     */
    void startAPICall() {
        try {
            JSONObject jsonObject = new JSONObject(
                    "{}"
            );
            // the actual request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.iextrading.com/1.0/stock/AAPL/batch?types=quote",
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, response.toString());
                            setContentView(R.layout.activity_main);
                            TextView tv = (TextView)findViewById(R.id.jsonResult);
                            tv.setText(response.toString());
                            ProgressBar progressBar = findViewById(R.id.progressBar);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            String net = error.networkResponse.toString();
                            Log.w(TAG, "Json Request failed: " + error.toString());
                            Log.e(TAG, net + "\ndata " + new String(error.networkResponse.data));
                            Toast.makeText(MainActivity.this, "Error " + error.toString(), Toast.LENGTH_LONG).show();
                            ProgressBar progressBar = findViewById(R.id.progressBar);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
            // add the request to the submission queue(async submit)
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
