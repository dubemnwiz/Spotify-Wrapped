package com.example.spotifysdk.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.spotifysdk.Editaccount;
import com.example.spotifysdk.LoginActivity;
import com.example.spotifysdk.LoginActivity2;
import com.example.spotifysdk.MainActivity;
import com.example.spotifysdk.MainActivity2;
import com.example.spotifysdk.R;
import com.example.spotifysdk.databinding.FragmentSettingsBinding;
import com.squareup.picasso.Picasso;

import java.util.Hashtable;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private TextView profile_name;
    private ImageView profile_image;
    private String username;
    private static final String IS_DARK = "isDark";
    private boolean isFirstSelection = true;
    private boolean isDialogShown = false;

    private static final String PREF_TIME_SPAN = "pref_time_span";
    public static SharedPreferences sharedPreferences;
    private Spinner spinner;

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
            int show = 0;
            @Override
            public void onClick(View view) {
                //onSpotifyConnectClicked(view);
                if (show == 0) {
                    LinearLayout spinLayout = root.findViewById(R.id.spinnerSettings);
                    spinLayout.setVisibility(View.VISIBLE);
                    Spinner spin = root.findViewById(R.id.spinnerBtn);
                    spin.setVisibility(View.VISIBLE);
                    //show = 1;
                } else {
                    LinearLayout spinLayout = root.findViewById(R.id.spinnerSettings);
                    spinLayout.setVisibility(View.GONE);
                    Spinner spin = root.findViewById(R.id.spinnerBtn);
                    spin.setVisibility(View.GONE);
                    show = 0;
                }

            }
        });

        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        spinner = root.findViewById(R.id.spinnerBtn);

        // Set the initial value of the spinner from SharedPreferences
        String savedTimeSpan = sharedPreferences.getString(PREF_TIME_SPAN, "default_time_span");
        int position = getPositionForTimeSpan(savedTimeSpan);
        spinner.setSelection(position);
        //spinner.setSelection(0);

        // Set an item selected listener to handle the selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("range", "switch ");
                // Check if it's the first selection (initial setup)
                if (!isFirstSelection) {
                    // Get the selected time span
                    String selectedTimeSpan = (String) parentView.getItemAtPosition(position);
                    // Handle the selected item
                    //showConfirmationDialog(selectedTimeSpan, spinner);
                    // Store the selected time span in SharedPreferences
                    sharedPreferences.edit().putString(PREF_TIME_SPAN, selectedTimeSpan).apply();

                    // Handle the selection as needed
                    handleItemSelected(selectedTimeSpan);
                } else {
                    // Update the flag to indicate that initial setup is done
                    isFirstSelection = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
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
        profile_image = (ImageView) root.findViewById(R.id.settings_image);
        MainActivity mainActivity = (MainActivity) requireActivity();
        String profile = mainActivity.getProfile();
        String profileName = mainActivity.getProfileName();
        Log.d("JSON", "Profile: " + profile);
        if (profile != null && profileName != null) {
            loadImageFromUrl(profile, profile_image);
            profile_name.setText(profileName);
        } else {
            Log.d("JSON", "Null Profile");
        }
        //username = getIntent().getStringExtra("key_user");
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    public void onSpotifyConnectClicked(View view) {
        Intent intent = new Intent(getActivity(), MainActivity2.class);
        startActivity(intent);
    }

    private void loadImageFromUrl(String imageUrl, ImageView view) {
        Picasso.get().load(imageUrl).into(view);
    }

    private void handleItemSelected(String selectedTimeSpan) {
        // Create an intent to start MainActivity2
        Intent intent = new Intent(getActivity(), MainActivity2.class);

        // Put the selected time span as an extra to the intent
        intent.putExtra("TIME_SPAN", selectedTimeSpan);

        // Start MainActivity2 with the intent
        startActivity(intent);
    }
    private int getPositionForTimeSpan(String timeSpan) {

        if (timeSpan.equals("6 Months")) {
            return 0;
        } else if (timeSpan.equals("1 Year")) {
            return 1;
        } else if (timeSpan.equals("1 Month")) {
            return 2;
        }else {
            Log.d("range", "shared: " + timeSpan);
            return 0; }
    }

    /*
    private void showConfirmationDialog(String selectedTimeSpan, Spinner spinner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("This will log you out, confirm if this is okay.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Perform logout action
                        Intent intent = new Intent(getActivity(), LoginActivity2.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        isDialogShown = false;
                        // Do nothing, cancel selection

                        // Reset the selection to the previous state
                        spinner.setSelection(0);
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        isDialogShown = false;
    }

     */

}
