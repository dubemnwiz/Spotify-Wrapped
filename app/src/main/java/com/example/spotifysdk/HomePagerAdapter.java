package com.example.spotifysdk;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentManager;

import com.example.spotifysdk.ui.settings.SettingsFragment;


public class HomePagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 4; // Number of pages

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SettingsFragment();
                /*
            case 1:
                return new ArtistsFragment();
            case 2:
                return new AlbumsFragment();
            case 3:
                return new GenresFragment();

                 */
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}