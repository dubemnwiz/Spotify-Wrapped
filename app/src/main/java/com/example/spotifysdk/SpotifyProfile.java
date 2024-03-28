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

public class SpotifyProfile extends AppCompatActivity {

    private TextView displayName, followerCount;
    private ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spotify_profile);

        // Initialize the views
        displayName = (TextView) findViewById(R.id.display_name);
        followerCount = (TextView) findViewById(R.id.followers);
        profilePic = (ImageView) findViewById(R.id.profile_pic);
        Button back = (Button) findViewById(R.id.backBtnProfile);

        try {
            JSONObject jsonObj = new JSONObject(getIntent().getStringExtra("data"));

            String displayNameE = jsonObj.getString("display_name");
            displayName.setText(displayNameE);

            JSONObject followersObject = jsonObj.getJSONObject("followers");
            int followerCountE = followersObject.getInt("total");
            followerCount.setText(followerCountE + " followers");

            JSONArray imagesArray = jsonObj.getJSONArray("images");
            if (imagesArray.length() > 0) {
                JSONObject imageObject = imagesArray.getJSONObject(0);
                String profilePictureUrl = imageObject.getString("url");
                loadImageFromUrl(profilePictureUrl);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(intent);
            }
        });

    }
    private void loadImageFromUrl(String imageUrl) {
        Picasso.get().load(imageUrl).into(profilePic);
    }

}
