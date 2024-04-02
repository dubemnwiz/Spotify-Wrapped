package com.example.spotifysdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        topartist_name = (TextView) findViewById(R.id.textView2);
        topartist_image = (ImageView) findViewById(R.id.topartist_image);
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
}