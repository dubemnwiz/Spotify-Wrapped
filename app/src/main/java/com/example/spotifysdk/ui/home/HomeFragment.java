package com.example.spotifysdk.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.spotifysdk.Editaccount;
import com.example.spotifysdk.HomePagerAdapter;
import com.example.spotifysdk.R;
import com.example.spotifysdk.databinding.FragmentHomeBinding;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private int[] tabLayouts = {R.layout.layout_overview, R.layout.layout_artists, R.layout.layout_tracks, R.layout.layout_genres};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;

        ViewPager viewPager = root.findViewById(R.id.view_pager);
        TabLayout tabLayout = root.findViewById(R.id.tab_layout);

        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        tabLayout.addTab(tabLayout.newTab().setText("Overview"));
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
}