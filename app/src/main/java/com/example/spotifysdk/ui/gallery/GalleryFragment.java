package com.example.spotifysdk.ui.gallery;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifysdk.R;
import com.example.spotifysdk.databinding.FragmentGalleryBinding;
import com.example.spotifysdk.MainActivity;
import com.example.spotifysdk.MainActivity2;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    private RecyclerView recyclerView; // For the RecyclerView
    private GalleryAdapter adapter; // For the adapter

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Retrieve screenshots from the Screenshots utility class
        List<Bitmap> screenshots = Screenshots.getScreenshots();

        recyclerView = view.findViewById(R.id.galleryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // Adjust columns as needed
        adapter = new GalleryAdapter(getContext(), screenshots);
        recyclerView.setAdapter(adapter);

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