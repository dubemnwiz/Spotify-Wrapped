package com.example.spotifysdk.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView topArtistTextView, ov_artist1, ov_artist2, ov_artist3, ov_artist4, ov_artist5;
    private TextView topSongTextView1;
    private TextView topSongTextView2;
    private TextView topSongTextView3;
    private TextView topSongTextView4;
    private TextView topSongTextView5;
    private ImageView topSongImage1;
    private ImageView topSongImage2;
    private ImageView topSongImage3;
    private ImageView topSongImage4;
    private ImageView topSongImage5;
    private ImageView topArtistImage, profilePic;
    private int[] tabLayouts = {R.layout.layout_overview, R.layout.layout_artists, R.layout.layout_tracks, R.layout.layout_genres};


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
        profilePic = root.findViewById(R.id.ov_image);
        MainActivity mainActivity = (MainActivity) requireActivity();
        List<String> topArtistsArray = mainActivity.fetchTop5Artists();
        if (topArtistsArray != null && !topArtistsArray.isEmpty()) {
            Log.d("JSON", "Top Songs " + topArtistsArray.get(0));
            ov_artist1.setText(topArtistsArray.get(0));
            ov_artist2.setText(topArtistsArray.get(1));
            ov_artist3.setText(topArtistsArray.get(2));
            ov_artist4.setText(topArtistsArray.get(3));
            ov_artist5.setText(topArtistsArray.get(4));
        }
        String profile = mainActivity.getProfile();
        Log.d("JSON", "Profile: " + profile);
        if (profile != null) {
            loadImageFromUrl(profile, profilePic);
        } else {
            Log.d("JSON", "Null Profile");
        }



    }

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

    }
}