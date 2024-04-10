package com.example.spotifysdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    SwitchCompat switcher;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private TextView topartist_name, playlistName1, playlistName2, playlistName3;
    private ImageView topartist_image, playlistPic1, playlistPic2, playlistPic3;
    @Override
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

    public List<String> fetchTop5Genres() {
        List<String> topGenres = new ArrayList<>();
        try {
            JSONObject artistsData = new JSONObject(getIntent().getStringExtra("topArtists"));
            JSONArray itemsArray = artistsData.getJSONArray("items");

            // Map to store genre occurrences
            Map<String, Integer> genreMap = new HashMap<>();

            // Iterate through each artist
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject artist = itemsArray.getJSONObject(i);
                JSONArray genresArray = artist.getJSONArray("genres");

                // Iterate through genres of each artist
                for (int j = 0; j < genresArray.length(); j++) {
                    String genre = genresArray.getString(j);
                    // Update genre occurrences
                    genreMap.put(genre, genreMap.getOrDefault(genre, 0) + 1);
                }
            }

            // Sort the genreMap by occurrences
            List<Map.Entry<String, Integer>> genreList = new ArrayList<>(genreMap.entrySet());
            genreList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            // Extract top 5 genres
            for (int i = 0; i < Math.min(5, genreList.size()); i++) {
                topGenres.add(genreList.get(i).getKey());
            }

            for (int i = 0; i < topGenres.size(); i++) {
                String genre = topGenres.get(i);
                topGenres.set(i, formatGenreString(genre));
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return topGenres;
    }

    // Helper method to format the genre strings
    private String formatGenreString(String input) {
        StringBuilder result = new StringBuilder(input.length());
        boolean capitalizeNext = true;
        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
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
            Log.d("Test", "Here: " + itemsArray);
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

    public List<String> fetchTopGenresImages() {
        List<String> topGenresImagesUrls = new ArrayList<>();
        try {
            // Extracting the JSON string from Intent extra
            String jsonData = getIntent().getStringExtra("topArtists");
            if (jsonData == null) {
                Log.e("fetchTopGenresImages", "No JSON data found in Intent extra 'topArtists'");
                return topGenresImagesUrls; // Early return to avoid NullPointerException
            }

            JSONObject jsonObj = new JSONObject(jsonData);
            JSONArray itemsArray = jsonObj.getJSONArray("items");

            // Loop through the items array, stopping at 5 or the array's length, whichever is smaller
            for (int i = 0; i < Math.min(5, itemsArray.length()); i++) {
                JSONObject artistObject = itemsArray.getJSONObject(i);
                JSONArray imagesArray = artistObject.getJSONArray("images");

                // If there are images, add the URL of the first image to the list
                if (imagesArray.length() > 0) {
                    JSONObject imageObject = imagesArray.getJSONObject(0); // Getting the first image
                    String imageUrl = imageObject.getString("url");
                    topGenresImagesUrls.add(imageUrl);
                    Log.d("JSON", "Genre Image: " + imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e("fetchTopGenresImages", "Error parsing JSON data", e);
        }
        return topGenresImagesUrls;
    }

    public List<String> fetchTopAlbum() {
        List<String> topSongs = new ArrayList<>();
        try {
            JSONObject tracksData = new JSONObject(getIntent().getStringExtra("topTracks"));
            JSONArray itemsArray = tracksData.getJSONArray("items");
            JSONObject trackObject = itemsArray.getJSONObject(1);
            JSONObject album = trackObject.getJSONObject("album");
            String trackName = album.getString("name");
            topSongs.add(trackName);
            Log.d("Album", "name: " + trackName);
            JSONArray albumArray = album.getJSONArray("images");
            JSONObject albumCover = albumArray.getJSONObject(1);
            String albumURL = albumCover.getString("url");
            topSongs.add(albumURL);
            Log.d("Album", "cover: " + albumURL);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return topSongs;
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