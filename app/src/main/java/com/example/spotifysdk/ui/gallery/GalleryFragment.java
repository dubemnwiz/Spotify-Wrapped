package com.example.spotifysdk.ui.gallery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifysdk.LoginActivity2;
import com.example.spotifysdk.R;
import com.example.spotifysdk.SpotifyWrappedDbHelper;
import com.example.spotifysdk.databinding.FragmentGalleryBinding;
import com.example.spotifysdk.MainActivity;
import com.example.spotifysdk.MainActivity2;
import com.example.spotifysdk.ui.home.HomeFragment;
import com.example.spotifysdk.ui.settings.SettingsFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private RecyclerView galleryRecyclerView; // For the RecyclerView
    private GalleryAdapter adapter; // For the adapter
    private List<GalleryItem> galleryItems = new ArrayList<>();
    public static Boolean imageClicked = false;
    public static int viewModelId = -1;

   // public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
     //   super.onViewCreated(view, savedInstanceState);
       // galleryRecyclerView = view.findViewById(R.id.galleryRecyclerView);
        //galleryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //adapter = new GalleryAdapter(getContext(), galleryItems);
       // galleryRecyclerView.setAdapter(adapter);

        //SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        //viewModel.getGalleryItems().observe(getViewLifecycleOwner(), items -> {
          //  galleryItems.clear();
           // galleryItems.addAll(items);
            //adapter.notifyDataSetChanged();
        //});
    //}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        // Access the ActionBar from the hosting Activity
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            // Set the title
            actionBar.setTitle("Past Wraps");
        }

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) GridLayout galleryLayout = view.findViewById(R.id.galleryGridLayout);

        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        MainActivity mainActivity = (MainActivity) requireActivity();
        String username = mainActivity.getUsername(); // Replace with the actual username

// Get ViewModel items associated with the current user
        SpotifyWrappedDbHelper dbHelper = new SpotifyWrappedDbHelper(getContext());
        Cursor cursor = dbHelper.getViewModelItems(username);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve ViewModel item data from the cursor
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date")); // Change "date" to match your column name
                //@SuppressLint("Range") String itemName = cursor.getString(cursor.getColumnIndex("item_name"));
                // Retrieve the ID from the cursor
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));

                // You can retrieve other ViewModel item data similarly

                // Create views for each ViewModel item
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(R.drawable.spotify_icon); // Use appropriate resource
                // Optionally set layout parameters, scale type, etc., for imageView

                // Set unique ID as tag for the ImageView
                imageView.setTag(id);

                TextView dateTextView = new TextView(getContext());
                dateTextView.setText(date);
                // Optionally style the TextView

                // Add views to the gallery layout
                galleryLayout.addView(imageView);
                galleryLayout.addView(dateTextView);

                // Set click listener for imageView
                imageView.setOnClickListener(v -> {
                    viewModelId = (int) v.getTag();
                    Log.d("tag99", "ID: " + (int) v.getTag());
                    viewModel.setImageClicked(true);
                    Toast.makeText(getContext(), "Past wrapped restored!", Toast.LENGTH_SHORT).show();
                });

            } while (cursor.moveToNext());

            cursor.close();
        } else {
            // Handle case when no ViewModel items found for the current user
            // For example, display a message or hide the gallery
            Toast.makeText(getContext(), "No past wraps found.", Toast.LENGTH_SHORT).show();
        }
//hi
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Screenshots.clearScreenshots();
    }
    private void captureAndSaveScreen() {
        if (getView() == null) return; // Ensure the view is available

        // Create a bitmap with the same dimensions as the view
        View view = getView().getRootView();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        // Add the bitmap to the Screenshots utility class
        Screenshots.addScreenshot(bitmap);
        // Optionally log or show a message
        Log.d("GalleryFragment", "Screenshot captured and stored in memory.");

    }
}