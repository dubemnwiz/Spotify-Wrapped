package com.example.spotifysdk.ui.home;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.spotifysdk.Editaccount;
import com.example.spotifysdk.HomePagerAdapter;
import com.example.spotifysdk.MainActivity;
import com.example.spotifysdk.R;
import com.example.spotifysdk.SpotifyProfile;
import com.example.spotifysdk.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView topArtistTextView, ov_artist1, ov_artist2, ov_artist3, ov_artist4, ov_artist5;
    private TextView ov_song1, ov_song2, ov_song3, ov_song4, ov_song5, ov_genre;
    private ImageView topArtistImage, profilePic;
    private int[] tabLayouts = {R.layout.layout_overview, R.layout.layout_artists, R.layout.layout_tracks, R.layout.layout_genres};

    private Button playButton;
    private MediaPlayer mediaPlayer;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;

        ViewPager viewPager = root.findViewById(R.id.view_pager);
        TabLayout tabLayout = root.findViewById(R.id.tab_layout);


        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("Overview");
        tabLayout.addTab(tab);
        tab.select();
        View tabContentView = inflater.inflate(tabLayouts[0], container, false);
        ViewGroup contentContainer = root.findViewById(R.id.content_container);
        contentContainer.addView(tabContentView);
        //tabLayout.addTab(tabLayout.newTab().setText("Overview"));
        tabLayout.addTab(tabLayout.newTab().setText("Top Artist"));
        tabLayout.addTab(tabLayout.newTab().setText("Top Songs"));
        tabLayout.addTab(tabLayout.newTab().setText("Top Genres"));
        tabLayout.addTab(tabLayout.newTab().setText("Top Genre"));
        tabLayout.addTab(tabLayout.newTab().setText("Top Song"));
        tabLayout.addTab(tabLayout.newTab().setText("Top Album"));
        tabLayout.addTab(tabLayout.newTab().setText("Recommendations"));




        //HomePagerAdapter adapter = new HomePagerAdapter(getFragmentManager());
        //viewPager.setAdapter(adapter);

        // Bind ViewPager with TabLayout
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    // Handle tab selection
                    int selectedPosition = tab.getPosition();
                    // Inflate layout for the selected tab
                    int layoutResId = tabLayouts[selectedPosition];
                    View tabContentView = inflater.inflate(layoutResId, container, false);
                    // Replace current content with the new tab content
                    ViewGroup contentContainer = root.findViewById(R.id.content_container);
                    contentContainer.removeAllViews();
                    contentContainer.addView(tabContentView);

                    switch (selectedPosition) {
                        case 0: // Top Artist tab
                            updateOverviewUI(root);
                            break;
                        case 1: // Overview tab
                            updateTopArtistUI(root);
                            break;
                        case 2: // Top Songs tab
                            updateTopSongsUI(root);
                            break;
                    }


                } catch (Exception e){
                    // Handle tab selection
                    int selectedPosition = tab.getPosition();
                    // Inflate layout for the selected tab
                    int layoutResId = tabLayouts[selectedPosition];
                    View tabContentView = inflater.inflate(layoutResId, container, false);
                    // Replace current content with the new tab content
                    ViewGroup contentContainer = root.findViewById(R.id.content_container);
                    contentContainer.removeAllViews();
                    contentContainer.addView(tabContentView);
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void addOnTabSelectedListener (TabLayout.OnTabSelectedListener listener) {

    }
    private void loadImageFromUrl(String imageUrl, ImageView view) {
        Picasso.get().load(imageUrl).into(view);
    }



    private void updateTopArtistUI(View root) {
        // Handle UI updates for the Top Artist tab
        // Code to set text and load image for top artist

        //TOP ARTIST TAB
        topArtistTextView = root.findViewById(R.id.textView2);
        topArtistImage = root.findViewById(R.id.topartist_image);
        MainActivity mainActivity = (MainActivity) requireActivity();
        String topArtistName = mainActivity.fetchTopArtist();
        if (topArtistName != null) {
            // Replace R.id.textView2 with the ID of your TextView
            topArtistTextView.setText(topArtistName);
        }
        String topArtistImageURL = mainActivity.fetchTopArtistImage();
        if (topArtistImage != null) {
            loadImageFromUrl(topArtistImageURL, topArtistImage);
        } else {
            Log.d("JSON", "Null");
        }

    }

    private void updateOverviewUI(View root) {
        // Handle UI updates for the Overview tab
        // Code to set text for top 5 artists
        //OVERVIEW TAB
        ov_artist1 = root.findViewById(R.id.artist1);
        ov_artist2 = root.findViewById(R.id.artist2);
        ov_artist3 = root.findViewById(R.id.artist3);
        ov_artist4 = root.findViewById(R.id.artist4);
        ov_artist5 = root.findViewById(R.id.artist5);
        ov_genre = root.findViewById(R.id.ov_genre);
        profilePic = root.findViewById(R.id.ov_image);
        MainActivity mainActivity = (MainActivity) requireActivity();
        List<String> topArtistsArray = mainActivity.fetchTop5Artists();
        if (topArtistsArray != null && !topArtistsArray.isEmpty()) {
            ov_artist1.setText(topArtistsArray.get(0));
            ov_artist2.setText(topArtistsArray.get(1));
            ov_artist3.setText(topArtistsArray.get(2));
            ov_artist4.setText(topArtistsArray.get(3));
            ov_artist5.setText(topArtistsArray.get(4));
            ov_genre.setText(topArtistsArray.get(5));
            Log.d("JSON", "Genres: " + topArtistsArray.get(5));
        }


        String profile = mainActivity.getProfile();

        if (profile != null) {
            loadImageFromUrl(profile, profilePic);
        } else {
            Log.d("JSON", "Null Profile");
        }

        ov_song1 = root.findViewById(R.id.song1);
        ov_song2 = root.findViewById(R.id.song2);
        ov_song3 = root.findViewById(R.id.song3);
        ov_song4 = root.findViewById(R.id.song4);
        ov_song5 = root.findViewById(R.id.song5);
        List<String> topSongsArray = mainActivity.parseTop5Songs();
        if (topSongsArray != null && !topSongsArray.isEmpty()) {
            //Log.d("JSON", "Top Songs " + topArtistsArray.get(0));
            ov_song1.setText(topSongsArray.get(0));
            ov_song2.setText(topSongsArray.get(1));
            ov_song3.setText(topSongsArray.get(2));
            ov_song4.setText(topSongsArray.get(3));
            ov_song5.setText(topSongsArray.get(4));
        }



    }
    List<String> songLinks;
    private void updateTopSongsUI(View root) {
        // Handle UI updates for the Top Songs tab
        // Code to display top song
        //TOP SONGS TAB
        MainActivity mainActivity = (MainActivity) requireActivity();
        List<String> topSongsArray = mainActivity.parseTop5Songs();

        List<String> topImagesArray = mainActivity.fetchTopSongsImages();
        // Text Views
        TextView[] topSongTextViews = new TextView[] {
                root.findViewById(R.id.TopSongName1),
                root.findViewById(R.id.TopSongName2),
                root.findViewById(R.id.TopSongName3),
                root.findViewById(R.id.TopSongName4),
                root.findViewById(R.id.TopSongName5)
        };
        ImageView[] topSongImages = new ImageView[] {
                root.findViewById(R.id.TopSongImage1),
                root.findViewById(R.id.TopSongImage2),
                root.findViewById(R.id.TopSongImage3),
                root.findViewById(R.id.TopSongImage4),
                root.findViewById(R.id.TopSongImage5)
        };
        // Setting Text
        if (topSongsArray != null && topSongsArray.size() >= 5) {
            try {

            } catch (Exception e) {
                Log.d("JSON", "Error " + e);
            }
            for (int i = 0; i < topSongTextViews.length; i++) {
                topSongTextViews[i].setText(topSongsArray.get(i));
            }
        }

        // Setting Images
        if (topImagesArray != null && topImagesArray.size() >= 5) {
            for (int i = 0; i < topSongImages.length; i++) {
                loadImageFromUrl(topImagesArray.get(i), topSongImages[i]);
            }
        } else {
            Log.d("JSON", "Null Songs");
            Log.d("JSON", "Null Images or insufficient images");
        }

        songLinks = mainActivity.top5SongLinks();
        if (songLinks != null) {
            Log.d("Testing", "Array: " + songLinks);
        }

        ImageButton track1 = root.findViewById(R.id.imageButton);
        track1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songLinks != null && songLinks.get(0) != null) {
                    playAudio(songLinks.get(0));
                } else {
                    Toast.makeText(getActivity(), "Song playback is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageButton track2 = root.findViewById(R.id.imageButton2);
        track2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songLinks != null && songLinks.get(4) != null) {
                    playAudio(songLinks.get(4));
                } else {
                    Toast.makeText(getActivity(), "Song playback is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageButton track3 = root.findViewById(R.id.imageButton3);
        track3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songLinks != null && songLinks.get(3) != null) {
                    playAudio(songLinks.get(3));
                } else {
                    Toast.makeText(requireContext(), "Song playback is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageButton track4 = root.findViewById(R.id.imageButton4);
        track4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songLinks != null && songLinks.get(2) != null) {
                    playAudio(songLinks.get(2));
                } else {
                    Toast.makeText(getContext(), "Song playback is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageButton track5 = root.findViewById(R.id.imageButton5);
        track5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songLinks != null && songLinks.get(1) != null) {
                    playAudio(songLinks.get(1));
                } else {
                    Toast.makeText(getContext(), "Song playback is not available.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void playAudio(String audioUrl) {
        if (audioUrl == null || audioUrl.isEmpty()) {
            Toast.makeText(getActivity(), "Song playback is not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync(); // prepare async to not block main thread
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}