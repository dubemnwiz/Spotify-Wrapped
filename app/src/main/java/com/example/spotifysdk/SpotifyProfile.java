package com.example.spotifysdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifysdk.ui.settings.SettingsFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class SpotifyProfile extends AppCompatActivity {

    private TextView displayName, followerCount, playlistName1, playlistName2, playlistName3;
    private ImageView profilePic, playlistPic1, playlistPic2, playlistPic3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spotify_profile);

        // Initialize the views
        displayName = (TextView) findViewById(R.id.display_name);
        followerCount = (TextView) findViewById(R.id.followers);
        profilePic = (ImageView) findViewById(R.id.profile_pic);

        playlistPic1 = (ImageView) findViewById(R.id.playlist_img1);
        playlistPic2 = (ImageView) findViewById(R.id.playlist_img2);
        playlistPic3 = (ImageView) findViewById(R.id.playlist_img3);

        playlistName1 = (TextView) findViewById(R.id.playlist_name1);
        playlistName2 = (TextView) findViewById(R.id.playlist_name2);
        playlistName3 = (TextView) findViewById(R.id.playlist_name3);
        Button back = (Button) findViewById(R.id.backBtnProfile);

        try {
            JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("data"));
            JSONObject jsonObj2 = new JSONObject(getIntent().getStringExtra("playlistsData"));

            String displayNameE = jsonObj.getString("display_name");
            displayName.setText(displayNameE);

            JSONObject followersObject = jsonObj.getJSONObject("followers");
            int followerCountE = followersObject.getInt("total");
            followerCount.setText(followerCountE + " followers");

            JSONArray imagesArray = jsonObj.getJSONArray("images");
            if (imagesArray.length() > 0) {
                JSONObject imageObject = imagesArray.getJSONObject(0);
                String profilePictureUrl = imageObject.getString("url");
                loadImageFromUrl(profilePictureUrl,profilePic);
            }

            JSONArray itemsArray = jsonObj2.getJSONArray("items");
            // Display top 3 playlists
            for (int i = 0; i < Math.min(3, itemsArray.length()); i++) {
                JSONObject playlistObject = itemsArray.getJSONObject(i);
                String playlistName = playlistObject.getString("name");
                JSONArray picsArray = playlistObject.getJSONArray("images");
                if (imagesArray.length() > 0) {
                    JSONObject imageObject = picsArray.getJSONObject(0); // Assuming you want to use the first image
                    String imageUrl = imageObject.getString("url");

                    switch (i) {
                        case 0:
                            playlistName1.setText(playlistName);
                            Picasso.get().load(imageUrl).into(playlistPic1);
                            break;
                        case 1:
                            playlistName2.setText(playlistName);
                            Picasso.get().load(imageUrl).into(playlistPic2);
                            break;
                        case 2:
                            playlistName3.setText(playlistName);
                            Picasso.get().load(imageUrl).into(playlistPic3);
                            break;
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
    private void loadImageFromUrl(String imageUrl, ImageView view) {
        Picasso.get().load(imageUrl).into(view);
    }

}