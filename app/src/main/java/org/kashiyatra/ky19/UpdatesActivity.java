package org.kashiyatra.ky19;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kashiyatra.ky19.adapters.UpdateAdapter;

import java.text.SimpleDateFormat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UpdatesActivity extends AppCompatActivity {
    RecyclerView updateRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updates);
        prefs = getSharedPreferences(SplashActivity.storeUserDetails, Context.MODE_PRIVATE);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getUpdates().execute();
            }
        });
        updateRecyclerView = findViewById(R.id.update_recycler);
        RecyclerView.LayoutManager updateLinearLayoutManager = new LinearLayoutManager(this);
        updateRecyclerView.setLayoutManager(updateLinearLayoutManager);

        new getUpdates().execute();

    }

    public class getUpdates extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... voids) {
            try {
                Request request = new Request.Builder()
                        .url("https://kashiyatra.herokuapp.com/api/mobile/notifications/")
                        .build();
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                JSONArray updates = new JSONArray(response.body().string());
                prefEditor = prefs.edit();
                prefEditor.putString("updates_json", updates.toString());
                prefEditor.commit();
                return updates;

            } catch (Exception e) {
                Log.e("UpdatesActivity", e.toString());
                e.printStackTrace();
            }
            return new JSONArray();
        }

        @Override
        protected void onPostExecute(JSONArray updates) {
            if (updates.length() == 0) {
                try {
                    Toast.makeText(getApplicationContext(), "Could not connect to server", Toast.LENGTH_SHORT).show();
                    updates = new JSONArray(prefs.getString("updates_json", "[\n" +
                            "    {\n" +
                            "        \"body\": \"No new updates. Check us out later\",\n" +
                            "        \"image_url\": null,\n" +
                            "        \"link\": \"http://www.kashiyatra.org/\",\n" +
                            "        \"timestamp\": \"2018-12-22T18:30:00Z\"\n" +
                            "    }\n" +
                            "]"));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("UpdatesActivity", e.toString());
                }
            }
            String[] bodies = new String[updates.length()];
            String[] imageUrls = new String[updates.length()];
            String[] links = new String[updates.length()];
            long[] times = new long[updates.length()];

            for (int i = 0; i < updates.length(); i++) {
                try {
                    JSONObject update = updates.getJSONObject(i);
                    bodies[i] = update.getString("body");
                    imageUrls[i] = update.getString("image_url");
                    times[i] = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                            .parse(update.getString("timestamp").replace("Z", "+00:00")).getTime();
                    links[i] = updates.getJSONObject(i).getString("link");
                } catch (Exception e) {
                    Log.e("Response Invalid", updates.toString());
                    e.printStackTrace();
                }
            }

            RecyclerView.Adapter updateAdapter = new UpdateAdapter(getApplicationContext(), bodies, imageUrls, links, times);
            updateRecyclerView.swapAdapter(updateAdapter, false);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
