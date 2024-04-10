package com.example.spotifysdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.spotifysdk.databinding.ActivityMainBinding;
import com.example.spotifysdk.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    public static final String CLIENT_ID = "98ff8d5e27094c31928d530b9e9704f0";
    public static final String REDIRECT_URI = "spotifysdk://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken;

    private Call mCall;

    SwitchCompat switcher;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private TextView topartist_name, playlistName1, playlistName2, playlistName3;
    private ImageView topartist_image, playlistPic1, playlistPic2, playlistPic3;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        topartist_name = (TextView) findViewById(R.id.textView2);
        topartist_image = findViewById(R.id.topartist_image);

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Top Artist Page


        String topArtistsData = getIntent().getStringExtra("topArtists");

        try {
            JSONObject jsonObj = new JSONObject(topArtistsData);
            JSONArray itemsArray = jsonObj.getJSONArray("items");

            if (itemsArray != null && itemsArray.length() > 0) {
                JSONObject topArtistObject = itemsArray.getJSONObject(0);
                String topArtistName = topArtistObject.getString("name");
                Log.d("JSON", "Top Artist: " + topArtistName);
                topartist_name.setText(topArtistName);

                Bundle bundle = new Bundle();
                bundle.putString("topArtistName", topArtistName);

                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);

                JSONArray topArtistImagesArray = topArtistObject.getJSONArray("images");
                if (topArtistImagesArray != null && topArtistImagesArray.length() > 0) {
                    JSONObject topArtistImageObject = topArtistImagesArray.getJSONObject(0);
                    String topArtistImageUrl = topArtistImageObject.getString("url");
                    loadImageFromUrl(topArtistImageUrl, topartist_image);
                    Log.d("JSON", "Image: " + topArtistImageUrl);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Find buttons by their IDs
        ImageButton playbackButton1 = findViewById(R.id.imageButton);
        ImageButton playbackButton2 = findViewById(R.id.imageButton2);
        ImageButton playbackButton3 = findViewById(R.id.imageButton3);
        ImageButton playbackButton4 = findViewById(R.id.imageButton4);
        ImageButton playbackButton5 = findViewById(R.id.imageButton5);
        // Add more buttons if needed
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static final String IS_DARK = "IS_DARK";

    @Override
    protected void attachBaseContext(Context baseContext) {
        super.attachBaseContext(baseContext);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(baseContext);
        boolean isDark = prefs.getBoolean(IS_DARK, false);

        if (isDark)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    private void applyTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkMode = prefs.getBoolean(IS_DARK, false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Recreate the activity to apply the theme changes
        recreate();
    }

    private void loadImageFromUrl(String imageUrl, ImageView view) {
        Picasso.get().load(imageUrl).into(view);
    }

    public String fetchTopArtist() {
        String topArtistName = null;
        try {
            JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("topArtists"));
            JSONArray itemsArray = jsonObj.getJSONArray("items");

            /** Display top 3 playlists */
            if (itemsArray != null && itemsArray.length() > 0) {
                JSONObject topArtistObject = itemsArray.getJSONObject(0);
                topArtistName = topArtistObject.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topArtistName;
    }

    public String fetchTopArtistImage() {
        String topArtistImageUrl = null;
        try {
            JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("topArtists"));
            JSONArray itemsArray = jsonObj.getJSONArray("items");

            if (itemsArray != null && itemsArray.length() > 0) {
                JSONObject topArtistObject = itemsArray.getJSONObject(0);
                JSONArray topArtistImagesArray = topArtistObject.getJSONArray("images");
                if (topArtistImagesArray.length() > 0) {
                    JSONObject topArtistImageObject = topArtistImagesArray.getJSONObject(0);
                    topArtistImageUrl = topArtistImageObject.getString("url");
                    Log.d("JSON", "Image: " + topArtistImageUrl);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return topArtistImageUrl;
    }

    public List<String> parseTop5Songs() {
        List<String> topSongs = new ArrayList<>();
        try {
            JSONObject tracksData = new JSONObject(getIntent().getStringExtra("topTracks"));
            JSONArray itemsArray = tracksData.getJSONArray("items");
            for (int i = 0; i < Math.min(5, itemsArray.length()); i++) {
                JSONObject trackObject = itemsArray.getJSONObject(i);
                String trackName = trackObject.getString("name");
                topSongs.add(trackName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topSongs;
    }

    public List<String> fetchTop5Artists() {
        List<String> topArtists = new ArrayList<>();
        try {
            JSONObject artistsData = new JSONObject(getIntent().getStringExtra("topArtists"));
            JSONArray itemsArray = artistsData.getJSONArray("items");
            for (int i = 0; i < Math.min(5, itemsArray.length()); i++) {
                JSONObject artistObject = itemsArray.getJSONObject(i);
                String artistName = artistObject.getString("name");
                topArtists.add(artistName);
            }
            JSONObject genres = itemsArray.getJSONObject(0);
            String genre = genres.getString("genres");
            genre = genre.substring(2, genre.length() - 2);
            genre = genre.replaceAll("\"", "");
            String[] genreArray = genre.split(",");
            topArtists.add(genreArray[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topArtists;
    }

    public String getProfile() {
        String profilePic = null;
        try {
            JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("data"));
            JSONArray imagesArray = jsonObj.getJSONArray("images");
            Log.d("JSON", "IMAGE URL " + jsonObj);
            if (imagesArray.length() > 0) {
                JSONObject imageObject = imagesArray.getJSONObject(0);
                profilePic = imageObject.getString("url");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return profilePic;
    }

    public String getProfileName() {
        String profileName = null;
        try {
            JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("data"));
            profileName = jsonObj.getString("display_name");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return profileName;
    }

    public List<String> fetchTopSongsImages() {
        List<String> topSongsImagesUrls = new ArrayList<>();
        try {
            // Extracting the JSON string from Intent extra
            String jsonData = getIntent().getStringExtra("topTracks");
            if (jsonData == null) {
                Log.e("fetchTopSongsImages", "No JSON data found in Intent extra 'topSongs'");
                return topSongsImagesUrls; // Early return to avoid NullPointerException
            }

            JSONObject jsonObj = new JSONObject(jsonData);
            JSONArray itemsArray = jsonObj.getJSONArray("items");

            // Loop through the items array, stopping at 5 or the array's length, whichever is smaller
            for (int i = 0; i < Math.min(5, itemsArray.length()); i++) {
                JSONObject songObject = itemsArray.getJSONObject(i);
                JSONObject albumObject = songObject.getJSONObject("album");
                JSONArray imagesArray = albumObject.getJSONArray("images");

                // If there are images, add the URL of the first image to the list
                if (imagesArray.length() > 0) {
                    JSONObject imageObject = imagesArray.getJSONObject(0); // Getting the first image
                    String imageUrl = imageObject.getString("url");
                    topSongsImagesUrls.add(imageUrl);
                    Log.d("JSON", "Song Image: " + imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e("fetchTopSongsImages", "Error parsing JSON data", e);
        }
        return topSongsImagesUrls;
    }

    public List<String> top5SongLinks() {
        List<String> songLinks = new ArrayList<>();
        try {
            JSONObject tracksData = new JSONObject(getIntent().getStringExtra("topTracks"));
            JSONArray itemsArray = tracksData.getJSONArray("items");
            for (int i = 0; i < Math.min(5, itemsArray.length()); i++) {
                JSONObject trackObject = itemsArray.getJSONObject(i);
                String link = trackObject.getString("preview_url");
                songLinks.add(link);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songLinks;
    }
}