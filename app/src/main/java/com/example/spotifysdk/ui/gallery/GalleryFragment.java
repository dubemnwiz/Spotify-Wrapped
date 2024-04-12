package com.example.spotifysdk.ui.gallery;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import com.example.spotifysdk.databinding.FragmentGalleryBinding;
import com.example.spotifysdk.MainActivity;
import com.example.spotifysdk.MainActivity2;
import com.example.spotifysdk.ui.home.HomeFragment;

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
        viewModel.getIconItems().observe(getViewLifecycleOwner(), icons -> {
            galleryLayout.removeAllViews(); // Clear existing views or adjust as needed
            int rowCount = (int) Math.ceil(icons.size() / 3.0); // Assuming 3 columns
            galleryLayout.setRowCount(icons.size() * 2);
            for (IconDateItem item : icons) {
                ImageView imageView = new ImageView(getContext());
                imageView.setImageResource(item.getIconResId());

                // Optionally set layout parameters, scale type, etc., for imageView
                imageView.setOnClickListener(v -> {
                    viewModel.setImageClicked(true);
                    Toast.makeText(getContext(), "Image clicked!", Toast.LENGTH_SHORT).show();
                });
                galleryLayout.addView(imageView);

                // Create and add a TextView for the date
                TextView dateTextView = new TextView(getContext());
                dateTextView.setText(item.getDate());
                // Optionally style the TextView

                galleryLayout.addView(dateTextView);
            }
        });
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