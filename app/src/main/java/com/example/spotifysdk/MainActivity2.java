package com.example.spotifysdk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;

import com.example.spotifysdk.ui.settings.SettingsFragment;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity2 extends AppCompatActivity {

    public static final String CLIENT_ID = "98ff8d5e27094c31928d530b9e9704f0";
    public static final String REDIRECT_URI = "spotifysdk://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;

    private String profileData;

    public static String Profile;

    private String artistsData;
    private String tracksData;
    private String recsData;
    private String seeds;
    private String URIData;
    private String successfulLogin = "Successfully connected to Spotify!";

    private TextView tokenTextView, codeTextView, profileTextView;

    private Hashtable<String, String> range_table = new Hashtable<>();

    private Spinner range_spinner;
    Boolean cantRun = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize the views
        tokenTextView = (TextView) findViewById(R.id.token_text_view);
        codeTextView = (TextView) findViewById(R.id.code_text_view);
        profileTextView = (TextView) findViewById(R.id.response_text_view);

        // Initialize the buttons
        Button tokenBtn = (Button) findViewById(R.id.token_btn);
        Button codeBtn = (Button) findViewById(R.id.code_btn);
        Button profileBtn = (Button) findViewById(R.id.profile_btn);
        Button back = findViewById(R.id.backBtnSpotify);
        Button loadBtn = (Button) findViewById((R.id.load_top_data));

        //Initialize spinner
        range_spinner = findViewById(R.id.range_spinner);

        getToken();
        //onLoadDataClicked();

        String selectedTimeSpan = getIntent().getStringExtra("TIME_SPAN");

        //Add values to HashTable
        range_table.put("1 Year", "long_term");
        range_table.put("6 Months", "medium_term");
        range_table.put("1 Month", "short_term");


        // Set the click listeners for the buttons

        tokenBtn.setOnClickListener((v) -> {
            getToken();
        });

        codeBtn.setOnClickListener((v) -> {
            getCode();
        });

        profileBtn.setOnClickListener((v) -> {
            onGetUserProfileClicked();
        });

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadDataClicked();
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsFragment.class);
                startActivity(intent);
            }
        });

        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        String username = getIntent().getStringExtra("key_user");
        intent.putExtra("username", username);
        Log.d("tag101", "onCreate: " + username);


    }

    /**
     * Get token from Spotify
     * This method will open the Spotify login activity and get the token
     * What is token?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(MainActivity2.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    /**
     * Get code from Spotify
     * This method will open the Spotify login activity and get the code
     * What is code?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(MainActivity2.this, AUTH_CODE_REQUEST_CODE, request);
    }


    /**
     * When the app leaves this activity to momentarily get a token/code, this function
     * fetches the result of that external activity to get the response from Spotify
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        // Check which request code is present (if any)
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {

            if (response != null && response.getAccessToken() != null && !response.getAccessToken().isEmpty()) {
                mAccessToken = response.getAccessToken();
                //setTextAsync(successfulLogin, tokenTextView);
                Intent intent = new Intent(MainActivity2.this, LoginActivity2.class);
                String username = getIntent().getStringExtra("key_user");
                //intent.putExtra("username", username);
                onLoadDataClicked();

                startActivity(intent);

            } else {
                Intent intent = new Intent(MainActivity2.this, LoginActivity2.class);
                String username = getIntent().getStringExtra("key_user");
                //intent.putExtra("username", username);

                startActivity(intent);
            }
        } else if (AUTH_CODE_REQUEST_CODE != requestCode) {
            if (response != null && response.getCode() != null && !response.getCode().isEmpty()) {
                mAccessCode = response.getCode();

                setTextAsync(mAccessCode, codeTextView);
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                String username = getIntent().getStringExtra("key_user");
                //intent.putExtra("username", username);
                startActivity(intent);


            } else {
                Intent intent = new Intent(MainActivity2.this, LoginActivity2.class);
                String username = getIntent().getStringExtra("key_user");
                //intent.putExtra("username", username);
                startActivity(intent);
            }
        }
    }



    /**
     * Get user top data
     * This method will get the user top data using the token
     */
    public void onLoadDataClicked() {
        if (mAccessToken == null) {
            //Toast.makeText(this, "You need to login to your Spotify first!", Toast.LENGTH_SHORT).show();
            return;
        }
        //Log.d("test55", "onLoadDataClicked: " + SettingsFragment.getTime());
        // Access SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Retrieve the time span preference with a default value of ""
        String test = sharedPreferences.getString("pref_time_span", "");



        String selectedTimeSpan = getIntent().getStringExtra("TIME_SPAN");
        if (!cantRun) {
            selectedTimeSpan = "6 Months";
            sharedPreferences.edit().putString("pref_time_span", selectedTimeSpan).apply();
            Log.d("test55", "Reset Case 1: " + test);
        }else if (!test.equals("")) {
            selectedTimeSpan = test;
            Log.d("test55", "Reset Case 2: " + test);
            Log.d("Test55", "onCase2: "+ cantRun);
        } else {
            selectedTimeSpan = "6 Months";
            sharedPreferences.edit().putString("pref_time_span", selectedTimeSpan).apply();
            cantRun = true;
            Log.d("test55", "Reset Case 3: " + test);
        }
        cantRun = true;
        // Get string value from table;
        String temp = range_table.get(selectedTimeSpan);
        Log.d("range", "Range: " + temp);
        // Save the selected time span to SharedPreferences

        // Create a request to get the user's top artists
        final Request topArtists = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?time_range="+temp)
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        // Create a request to get the user's top tracks
        final Request topTracks = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?time_range="+temp)
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();



        //Create a request to get the user's profile
        final Request profileRequest = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        //Create a request to get the user's profile
        final Request recArtists = new Request.Builder()
                .url("https://api.spotify.com/v1/artists?id=")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(topArtists);

        /**
         * Making API call for top artists
         */
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(MainActivity2.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject artistJsonObject = new JSONObject(response.body().string());
                    artistsData = artistJsonObject.toString();
                    if (artistsData == null || artistJsonObject.getJSONArray("items").isNull(0)) {
                        cantRun = false;
                        onLoadDataClicked();
                        /*
                        Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
                        startActivity(intent);
                        finish();

                         */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(MainActivity2.this, "You do not have enough data for this time span!",
                                        Toast.LENGTH_SHORT).show();

                            }
                        });

                        //Intent intent = new Intent(MainActivity2.this, LoginActivity2.class);
                        //startActivity(intent);
                        return;
                    }
                    Log.d("Test35", "onResponse: " + artistsData);
                    seeds = fetchArtistsSeeds(artistsData);
                    relatedArtists(seeds);
                    fetchTracks(topTracks, artistsData, profileData, URIData, recsData);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCall = mOkHttpClient.newCall(profileRequest);

        /**
         * Making API call for profile data
         */
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(MainActivity2.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    profileData = jsonObject.toString();
                    Log.d("tag22", "tes2: " + profileData);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public String relatedArtists(String seeds) {
        Log.d("tag22", "onLoad: " + seeds);
        // Create a request to get the user's top recommended
        final Request topRecs = new Request.Builder()
                .url("https://api.spotify.com/v1/artists/" + seeds + "/related-artists")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();


        mCall = mOkHttpClient.newCall(topRecs);

        /**
         * Making API call for recommendations
         */
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(MainActivity2.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    recsData = jsonObject.toString();
                    Log.d("tag22", "testing2: " + recsData);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return recsData;
    }



    /**
     * Get user profile
     * This method will get the user profile using the token
     */
    public void onGetUserProfileClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to login to your Spotify first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user profileBtn
        final Request profileRequest = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        // Create a request to get the user's playlists
        final Request playlistsRequest = new Request.Builder()
                .url("https://api.spotify.com/v1/me/playlists")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(profileRequest);

        /**
         * Making API call for profile data
         */
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(MainActivity2.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    profileData = jsonObject.toString();
                    Profile = profileData;
                    fetchPlaylists(playlistsRequest, profileData);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    MainActivity2.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * Get user playlists
     * This method will get the user's playlists using the token
     */
    private void fetchPlaylists(Request request, final String profileData) {
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch playlists: " + e);
                Toast.makeText(MainActivity2.this, "Failed to fetch playlists, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject playlistsJsonObject = new JSONObject(response.body().string());
                    final String playlistsData = playlistsJsonObject.toString();
                    startNewActivity(profileData, playlistsData);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse playlists data: " + e);
                }
            }
        });
    }

    /**
     * Get user top tracks
     * This method will get the user's top tracks using the token
     */
    List<String> topTrackURIs;
    private void fetchTracks(Request request, final String artistsData, String profileData, String uriData, String recsData) {
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(MainActivity2.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject tracksJsonObject = new JSONObject(response.body().string());
                    topTrackURIs = parseTopTrackURIs(tracksJsonObject); // Parse the top track URIs
                    tracksData = tracksJsonObject.toString();
                    startMainActivity(artistsData, tracksData, profileData, recsData);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to parse the top track URI from the JSON response
    private List<String> parseTopTrackURIs(JSONObject tracksJsonObject) throws JSONException {
        List<String> topTrackURIs = new ArrayList<>();
        JSONArray itemsArray = tracksJsonObject.getJSONArray("items");
        for (int i = 0; i < Math.min(5, itemsArray.length()); i++) {
            JSONObject trackObject = itemsArray.getJSONObject(i);
            String trackURI = trackObject.getString("uri");
            topTrackURIs.add(trackURI);
        }
        return topTrackURIs;
    }

    public List<String> getTrackURI() {
        return topTrackURIs;
    }

    //Create a request to add songs to queue
    final Request queue = new Request.Builder()
            .url("https://api.spotify.com/v1/me/player/queue")
            .addHeader("Authorization", "Bearer " + mAccessToken)
            .build();

    public String fetchArtistsSeeds(String artistsJSON) {
        List<String> seeds = new ArrayList<>();
        try {
            JSONObject artistsData = new JSONObject(artistsJSON);
            JSONArray itemsArray = artistsData.getJSONArray("items");
            for (int i = 0; i < Math.min(5, itemsArray.length()); i++) {
                JSONObject artistObject = itemsArray.getJSONObject(i);
                String artistsId = artistObject.getString("id");
                seeds.add(artistsId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("seeds", "fetchArtistsSeeds: " + seeds.toString());
        if (!seeds.isEmpty()) {
            return seeds.get(0);
        } else {
            /*
            Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                    Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(MainActivity2.this, LoginActivity2.class);
            //startActivity(intent);

             */

            return "null";
        }
    }

    public String getSeeds() {
        return seeds;
    }

    /**
     * Starts the profile view activity
     * Puts the data from the API calls into the profile activity for parsing
     * The JSON is currently in the form of a String (Not JSONObject)
     */
    private void startNewActivity(String data, String playlistsData) {
        Intent intent = new Intent(MainActivity2.this, SpotifyProfile.class);
        intent.putExtra("data", data);
        intent.putExtra("playlistsData", playlistsData);
        startActivity(intent);
    }

    /**
     * Starts the profile view activity
     * Puts the data from the API calls into the profile activity for parsing
     * The JSON is currently in the form of a String (Not JSONObject)
     */

    private void startMainActivity(String artistsData, String tracksData, String data, String recsData) throws JSONException {
        JSONObject test2 = new JSONObject(artistsData);
        JSONArray itemsArray = test2.getJSONArray("items");
        Log.d("test35", "on: " + itemsArray);
        if (itemsArray.isNull(0)) {
            Intent intent = new Intent(MainActivity2.this, MainActivity2.class);
            startActivity(intent);
            finish();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity2.this, "You do not have enough data for this time span!",
                            Toast.LENGTH_SHORT).show();

                }
            });
            Log.d("Test35", "onResponse: Null data");
            //Intent intent = new Intent(MainActivity2.this, LoginActivity2.class);
            //startActivity(intent);
            return;
        }
        String test = relatedArtists(seeds);
        Log.d("JSON", "Test: " + test);
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        intent.putExtra("data", data);
        intent.putExtra("topArtists", artistsData);
        intent.putExtra("topTracks", tracksData);
        intent.putExtra("topTrackURI", URIData);
        intent.putExtra("topRecs", test);

        String username = getIntent().getStringExtra("key_user");
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * Creates a UI thread to update a TextView in the background
     * Reduces UI latency and makes the system perform more consistently
     *
     * @param text the text to set
     * @param textView TextView object to update
     */
    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }

    /**
     * Get authentication request
     *
     * @param type the type of the request
     * @return the authentication request
     */
    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-read-email", "playlist-read-private", "user-top-read"}) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

    /**
     * Gets the redirect Uri for Spotify
     *
     * @return redirect Uri object
     */
    private Uri getRedirectUri() { return Uri.parse(REDIRECT_URI); }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }
}