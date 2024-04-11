package com.example.spotifysdk.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.spotifysdk.Editaccount;
import com.example.spotifysdk.HomePagerAdapter;
import com.example.spotifysdk.MainActivity;
import com.example.spotifysdk.MainActivity2;
import com.example.spotifysdk.R;
import com.example.spotifysdk.SpotifyProfile;
import com.example.spotifysdk.SpotifyWrappedDbHelper;
import com.example.spotifysdk.databinding.FragmentHomeBinding;
import com.example.spotifysdk.ui.gallery.GalleryFragment;
import com.example.spotifysdk.ui.gallery.GalleryItem;
import com.example.spotifysdk.ui.gallery.IconDateItem;
import com.example.spotifysdk.ui.gallery.SharedViewModel;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    List<String> topArtistsArray;
    List<String> topSongsArray;
    List<String> topImagesArray;
    List<String> topGenresArray;
    List<String> topAlbumArray;
    List<String> topGenresImagesArray;
    String artistImage;
    private TextView topArtistTextView, ov_artist1, ov_artist2, ov_artist3, ov_artist4, ov_artist5;
    private TextView ov_song1, ov_song2, ov_song3, ov_song4, ov_song5, ov_genre;
    private TextView tv_genre, tv_song, tv_album;
    private ImageView topArtistImage, profilePic;
    private ImageView topsong_image, topgenre_image, topalbum_image;
    private SpotifyWrappedDbHelper dbHelper;
    private SharedViewModel viewModel;
    private Boolean isClicked = false;
    private int[] tabLayouts = {R.layout.layout_overview, R.layout.layout_artists, R.layout.layout_tracks, R.layout.layout_genres, R.layout.top_genre, R.layout.top_song, R.layout.top_album, R.layout.layout_recom};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;

        ViewPager viewPager = root.findViewById(R.id.view_pager);
        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        // Initialize the dbHelper
        dbHelper = new SpotifyWrappedDbHelper(getContext());

        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("Overview");
        tabLayout.addTab(tab);
        tab.select();
        View tabContentView = inflater.inflate(tabLayouts[0], container, false);
        ViewGroup contentContainer = root.findViewById(R.id.content_container);
        contentContainer.addView(tabContentView);
        try {
            updateOverviewUI(root);
        } catch (Exception e) {

        }

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
                        case 3: // Top Genres tab
                            updateTopGenres(root);
                            break;
                        case 4: // Top Genre tab
                            updateTopGenre(root);
                            break;
                        case 5: // Top Song tab
                            updateTopSong(root);
                            break;
                        case 6: // Top Song tab
                            updateTopAlbum(root);
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

        Button shareButton = root.findViewById(R.id.btn_share); // Replace with your share button ID
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndSharePDF(root);
            }
        });

        Button saveButton = root.findViewById(R.id.save);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        saveButton.setOnClickListener(v -> {
            // Get current date in desired format
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            // Create new IconDateItem
            IconDateItem newItem = new IconDateItem(R.drawable.spotify_icon, currentDate);
            updateDatabase();
            // Add to ViewModel
            viewModel.addIconItem(newItem);
        });
        viewModel.imageClicked.observe(getViewLifecycleOwner(), clicked -> {
            if (clicked) {
                isClicked = true;
            }
        });
        isClicked = false;

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
        if(isClicked) {
            // Retrieving data from Artists table
            topArtistsArray.clear();
            Cursor artistCursor = dbHelper.getAllArtists();
            if (artistCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String artistName = artistCursor.getString(artistCursor.getColumnIndex("name"));
                    @SuppressLint("Range") String artistImageUrl = artistCursor.getString(artistCursor.getColumnIndex("image_url"));
                    // Add the retrieved data to artistList
                    topArtistsArray.add(artistName);
                    artistImage = artistImageUrl;
                } while (artistCursor.moveToNext());
            }
            artistCursor.close();
        }
        if (topArtistName != null) {
            // Replace R.id.textView2 with the ID of your TextView
            topArtistTextView.setText(topArtistName);
        }
        String topArtistImageURL = mainActivity.fetchTopArtistImage();
        if (isClicked) {
            topArtistImageURL = artistImage;
        }
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
        topArtistsArray = mainActivity.fetchTop5Artists();
        if (isClicked) {
            topArtistsArray.clear();
            Cursor artistCursor = dbHelper.getAllArtists();
            if (artistCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String artistName = artistCursor.getString(artistCursor.getColumnIndex("name"));
                    @SuppressLint("Range") String artistImageUrl = artistCursor.getString(artistCursor.getColumnIndex("image_url"));
                    // Add the retrieved data to artistList
                    topArtistsArray.add(artistName);
                    artistImage = artistImageUrl;
                } while (artistCursor.moveToNext());
            }
            artistCursor.close();
        }
        if (topArtistsArray != null && !topArtistsArray.isEmpty()) {
            ov_artist1.setText(topArtistsArray.get(0));
            ov_artist2.setText(topArtistsArray.get(1));
            ov_artist3.setText(topArtistsArray.get(2));
            ov_artist4.setText(topArtistsArray.get(3));
            ov_artist5.setText(topArtistsArray.get(4));
            ov_genre.setText(topArtistsArray.get(5));
            Log.d("JSON", "Genres: " + topArtistsArray.get(5));
        }

        if (!isClicked) {
            String profile = mainActivity.getProfile();
            if (profile != null ) {
                loadImageFromUrl(profile, profilePic);
            } else {
                Log.d("JSON", "Null Profile");
            }
        }

        ov_song1 = root.findViewById(R.id.song1);
        ov_song2 = root.findViewById(R.id.song2);
        ov_song3 = root.findViewById(R.id.song3);
        ov_song4 = root.findViewById(R.id.song4);
        ov_song5 = root.findViewById(R.id.song5);
        topSongsArray = mainActivity.parseTop5Songs();
        if (isClicked) {
            // Retrieving data from Songs table
            topSongsArray.clear();
            Cursor songCursor = dbHelper.getAllSongs();
            if (songCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String songName = songCursor.getString(songCursor.getColumnIndex("name"));
                    @SuppressLint("Range") String songImageUrl = songCursor.getString(songCursor.getColumnIndex("image_url"));
                    // Add the retrieved data to songList
                    topSongsArray.add(songName);
                    topImagesArray.add(songImageUrl);
                } while (songCursor.moveToNext());
            }
            songCursor.close();
        }
        if (topSongsArray != null && !topSongsArray.isEmpty()) {
            //Log.d("JSON", "Top Songs " + topArtistsArray.get(0));
            ov_song1.setText(topSongsArray.get(0));
            ov_song2.setText(topSongsArray.get(1));
            ov_song3.setText(topSongsArray.get(2));
            ov_song4.setText(topSongsArray.get(3));
            ov_song5.setText(topSongsArray.get(4));
        }



    }

    private void updateTopSongsUI(View root) {
        // Handle UI updates for the Top Songs tab
        // Code to display top song
        //TOP SONGS TAB
        MainActivity mainActivity = (MainActivity) requireActivity();
        topSongsArray = mainActivity.parseTop5Songs();
        topImagesArray = mainActivity.fetchTopSongsImages();
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
        if (isClicked) {
            topSongsArray.clear();
            topImagesArray.clear();
            // Retrieving data from Songs table
            Cursor songCursor = dbHelper.getAllSongs();
            if (songCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String songName = songCursor.getString(songCursor.getColumnIndex("name"));
                    @SuppressLint("Range") String songImageUrl = songCursor.getString(songCursor.getColumnIndex("image_url"));
                    // Add the retrieved data to songList
                    topSongsArray.add(songName);
                    topImagesArray.add(songImageUrl);
                } while (songCursor.moveToNext());
            }
            songCursor.close();
        }
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

    private void updateTopGenres(View root) {
        // Handle UI updates for the Top Genres tab
        // Code to display top genres
        //TOP GENRES TAB
        MainActivity mainActivity = (MainActivity) requireActivity();
        topGenresArray = mainActivity.fetchTop5Genres();
        if (isClicked) {
            topGenresArray.clear();
            // Retrieving data from Genres table
            Cursor genreCursor = dbHelper.getAllGenres();
            if (genreCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String genreName = genreCursor.getString(genreCursor.getColumnIndex("name"));
                    @SuppressLint("Range") String genreImageUrl = genreCursor.getString(genreCursor.getColumnIndex("image_url"));
                    // Add the retrieved data to genreList
                    topGenresArray.add(genreName);
                } while (genreCursor.moveToNext());
            }
            genreCursor.close();
        }
        // Text Views
        TextView[] topGenreTextViews = new TextView[] {
                root.findViewById(R.id.genre1),
                root.findViewById(R.id.genre2),
                root.findViewById(R.id.genre3),
                root.findViewById(R.id.genre4),
                root.findViewById(R.id.genre5)
        };
        // Setting Text
        if (topGenresArray != null && topGenresArray.size() >= 5) {
            try {

            } catch (Exception e) {
                Log.d("JSON", "Error " + e);
            }
            for (int i = 0; i < topGenreTextViews.length; i++) {
                topGenreTextViews[i].setText(topGenresArray.get(i));
            }
        }


    }

    private void updateTopSong(View root) {
        //TOP SONG TAB
        tv_song = root.findViewById(R.id.tv_song);
        topsong_image = root.findViewById(R.id.topsong_image);
        MainActivity mainActivity = (MainActivity) requireActivity();
        topSongsArray = mainActivity.parseTop5Songs();
        if (isClicked) {
            topSongsArray.clear();
            topImagesArray.clear();
            // Retrieving data from Songs table
            Cursor songCursor = dbHelper.getAllSongs();
            if (songCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String songName = songCursor.getString(songCursor.getColumnIndex("name"));
                    @SuppressLint("Range") String songImageUrl = songCursor.getString(songCursor.getColumnIndex("image_url"));
                    // Add the retrieved data to songList
                    topSongsArray.add(songName);
                    topImagesArray.add(songImageUrl);
                } while (songCursor.moveToNext());
            }
            songCursor.close();
        }
        if (topSongsArray != null && !topSongsArray.isEmpty()) {
            //Log.d("JSON", "Top Songs " + topArtistsArray.get(0));
            tv_song.setText(topSongsArray.get(0));
        }
        topImagesArray = mainActivity.fetchTopSongsImages();
        if (isClicked) {
            topSongsArray.clear();
            topImagesArray.clear();
            // Retrieving data from Songs table
            Cursor songCursor = dbHelper.getAllSongs();
            if (songCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String songName = songCursor.getString(songCursor.getColumnIndex("name"));
                    @SuppressLint("Range") String songImageUrl = songCursor.getString(songCursor.getColumnIndex("image_url"));
                    // Add the retrieved data to songList
                    topSongsArray.add(songName);
                    topImagesArray.add(songImageUrl);
                } while (songCursor.moveToNext());
            }
            songCursor.close();
        }
        if (topImagesArray != null && !topImagesArray.isEmpty()) {
            //Log.d("JSON", "Top Songs " + topArtistsArray.get(0));
            loadImageFromUrl(topImagesArray.get(0), topsong_image);
        }



    }
    private void updateTopGenre(View root) {
        //TOP SONG TAB

        tv_genre = root.findViewById(R.id.tv_genre);
        topgenre_image = root.findViewById(R.id.topgenre_image);
        MainActivity mainActivity = (MainActivity) requireActivity();
        topSongsArray = mainActivity.fetchTop5Genres();
        if (isClicked) {
            topSongsArray.clear();
            // Retrieving data from Genres table
            Cursor genreCursor = dbHelper.getAllGenres();
            if (genreCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String genreName = genreCursor.getString(genreCursor.getColumnIndex("name"));
                    @SuppressLint("Range") String genreImageUrl = genreCursor.getString(genreCursor.getColumnIndex("image_url"));
                    // Add the retrieved data to genreList
                    topGenresArray.add(genreName);
                } while (genreCursor.moveToNext());
            }
            genreCursor.close();
        }
        if (topSongsArray != null && !topSongsArray.isEmpty()) {
            //Log.d("JSON", "Top Songs " + topArtistsArray.get(0));
            tv_genre.setText(topSongsArray.get(0));
        }
        List<String> topGenreImageURL = mainActivity.fetchTopGenresImages();
        if (isClicked) {
            topGenresImagesArray.clear();
            // Retrieving data from Genres table
            Cursor genreCursor = dbHelper.getAllGenres();
            if (genreCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String genreName = genreCursor.getString(genreCursor.getColumnIndex("name"));
                    @SuppressLint("Range") String genreImageUrl = genreCursor.getString(genreCursor.getColumnIndex("image_url"));
                    // Add the retrieved data to genreList
                    //topGenresArray.add(genreName);
                    topGenresImagesArray.add(genreImageUrl);
                } while (genreCursor.moveToNext());
            }
            genreCursor.close();
        }
        if (topGenreImageURL != null) {
            loadImageFromUrl(topGenreImageURL.get(0), topgenre_image);
        }

    }

    private void updateTopAlbum(View root) {
        //TOP SONG TAB
        tv_album = root.findViewById(R.id.tv_album);
        topalbum_image = root.findViewById(R.id.topalbum_image);
        MainActivity mainActivity = (MainActivity) requireActivity();
        topAlbumArray = mainActivity.fetchTopAlbum();
        if (isClicked) {
            topAlbumArray.clear();
            // Retrieving data from Albums table
            Cursor albumCursor = dbHelper.getAllAlbums();
            if (albumCursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String albumName = albumCursor.getString(albumCursor.getColumnIndex("name"));
                    @SuppressLint("Range") String albumImageUrl = albumCursor.getString(albumCursor.getColumnIndex("image_url"));
                    // Add the retrieved data to albumList
                    topAlbumArray.add(albumName);
                    topAlbumArray.add(albumImageUrl);
                } while (albumCursor.moveToNext());
            }
            albumCursor.close();
        }
        if (topAlbumArray != null && !topAlbumArray.isEmpty()) {
            //Log.d("JSON", "Top Songs " + topArtistsArray.get(0));
            tv_album.setText(topAlbumArray.get(0));
            loadImageFromUrl(topAlbumArray.get(1), topalbum_image);
        }

    }


    private void updateDatabase() {
        MainActivity mainActivity = (MainActivity) requireActivity();
        List<String> topArtistsArray = mainActivity.fetchTop5Artists();
        List<String> topSongsArray = mainActivity.parseTop5Songs();
        List<String> topAlbumsArray = mainActivity.fetchTopAlbum();
        List<String> topGenresArray = mainActivity.fetchTop5Genres();
        List<String> topSongsImagesArray = mainActivity.fetchTopSongsImages();
        List<String> topGenresImagesArray = mainActivity.fetchTopGenresImages();
        for (int i = 0; i < topSongsArray.size(); i++) {
            dbHelper.insertSong(topSongsArray.get(i), topSongsImagesArray.get(i));
            dbHelper.insertArtist(topArtistsArray.get(i), mainActivity.fetchTopArtistImage());
            dbHelper.insertGenre(topGenresArray.get(i), topGenresImagesArray.get(i));
        }
        dbHelper.insertAlbum(topAlbumsArray.get(0), topAlbumsArray.get(1));
    }
    private void generateAndSharePDF(View rootView) {
        // Create a bitmap from the view
        Bitmap bitmap = createBitmapFromView(rootView);

        // Create a PDF document
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // Draw the bitmap on the PDF page
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.WHITE);
        canvas.drawPaint(paint);
        canvas.drawBitmap(bitmap, 0, 0, null);

        // Finish the page
        pdfDocument.finishPage(page);

        // Save the PDF file
        File pdfFile = savePDF(pdfDocument);

        // Share the PDF file
        if (pdfFile != null) {
            sharePDF(pdfFile);
        } else {
            Toast.makeText(requireContext(), "Error creating PDF", Toast.LENGTH_SHORT).show();
        }

        // Close the PDF document
        pdfDocument.close();
    }

    // Method to create a bitmap from a view
    private Bitmap createBitmapFromView(View view) {
        // Get the width and height of the view
        int width = view.getWidth();
        int height = view.getHeight();

        // Create a bitmap with the same width and height as the view
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        // Create a canvas with the bitmap
        Canvas canvas = new Canvas(bitmap);

        // Draw the view onto the canvas
        view.draw(canvas);

        return bitmap;
    }

    // Method to save the PDF file
    private File savePDF(PdfDocument pdfDocument) {
        // Create a file to save the PDF
        File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "overview.pdf");

        try {
            // Create a file output stream
            FileOutputStream outputStream = new FileOutputStream(pdfFile);

            // Write the PDF document to the output stream
            pdfDocument.writeTo(outputStream);

            // Close the output stream
            outputStream.close();

            return pdfFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sharePDF(File file) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        Uri uri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", file);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share PDF"));
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


}