package com.example.spotifysdk;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifysdk.ui.settings.SettingsFragment;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Hashtable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    private String successfulLogin = "Successfully connected to Spotify!";

    private TextView tokenTextView, codeTextView, profileTextView;

    private Hashtable<String, String> range_table = new Hashtable<>();

    private Spinner range_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Initialize the views
        tokenTextView = (TextView) findViewById(R.id.token_text_view);
        codeTextView = (TextView) findViewById(R.id.code_text_view);
        profileTextView = (TextView) findViewById(R.id.response_text_view);

        range_spinner = findViewById(R.id.range_spinner);

        // Initialize the buttons
        Button tokenBtn = (Button) findViewById(R.id.token_btn);
        Button codeBtn = (Button) findViewById(R.id.code_btn);
        Button profileBtn = (Button) findViewById(R.id.profile_btn);
        Button back = findViewById(R.id.backBtnSpotify);
        Button loadBtn = (Button) findViewById((R.id.load_top_data));

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

        //Add values to HashTable
        range_table.put("1 Year", "long_term");
        range_table.put("6 Months", "medium_term");
        range_table.put("1 Month", "short_term");

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
            mAccessToken = response.getAccessToken();
            setTextAsync(successfulLogin, tokenTextView);

        } else if (AUTH_CODE_REQUEST_CODE == requestCode) {
            mAccessCode = response.getCode();
        }
    }

    /**
     * Get user top data
     * This method will get the user top data using the token
     */
    public void onLoadDataClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to login to your Spotify first!", Toast.LENGTH_SHORT).show();
            return;
        }

        String temp = range_spinner.getSelectedItem().toString();
        temp = range_table.get(temp);

        setTextAsync(mAccessCode, codeTextView);
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

        final Request profileRequest = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
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
                    fetchTracks(topTracks, artistsData, profileData);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mCall = mOkHttpClient.newCall(profileRequest);

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
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    profileData = jsonObject.toString();
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    private void fetchTracks(Request request, final String artistsData, String profileData) {
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
                    tracksData = tracksJsonObject.toString();
                    startMainActivity(artistsData, tracksData, profileData);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(MainActivity2.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    private void startMainActivity(String artistsData, String tracksData, String data) {
        Log.d("JSON", "Failed: " + data);
        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        intent.putExtra("data", data);
        intent.putExtra("topArtists", artistsData);
        intent.putExtra("topTracks", tracksData);
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