package com.example.spotifysdk.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.spotifysdk.Editaccount;
import com.example.spotifysdk.LoginActivity;
import com.example.spotifysdk.R;
import com.example.spotifysdk.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private TextView profile_name;
    private String username;
    private static final String IS_DARK = "isDark";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSettings;
        settingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button btnAccount = binding.btnAccountinfo;
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use Navigation component to navigate to AccountFragment
                Intent intent = new Intent(getActivity(), Editaccount.class);
                startActivity(intent);
            }
        });

        // Connect to Spotify links to spotify connect page
        RelativeLayout spotifyConnectLayout = root.findViewById(R.id.spotifyConnectLayout);
        spotifyConnectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSpotifyConnectClicked(view);
            }
        });

        // Logout goes to sign in page
        RelativeLayout logoutButton = root.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        SwitchCompat switchDarkMode = root.findViewById(R.id.switchDarkMode);

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update UI based on the switch state
                if (isChecked) {
                    // Dark Mode is enabled
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    // Dark Mode is disabled
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });

        profile_name = (TextView) root.findViewById(R.id.settings_name);
        //username = getIntent().getStringExtra("key_user");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onSpotifyConnectClicked(View view) {

        Navigation.findNavController(view).navigate(R.id.nav_account_info);
    }

    /*
    public void getUserDetails() {
        DBHelper dbHelper = new DBHelper(getActivity());
        ArrayList<User> list = dbHelper.getUserData(username);
        User usermodel = list.get(0);
    }

     */

}
