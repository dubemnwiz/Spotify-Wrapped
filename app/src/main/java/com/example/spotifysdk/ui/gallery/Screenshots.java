package com.example.spotifysdk.ui.gallery;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Screenshots {
    private static final List<Bitmap> screenshots = new ArrayList<>();

    public static void addScreenshot(Bitmap screenshot) {
        screenshots.add(screenshot);
    }

    public static List<Bitmap> getScreenshots() {
        return new ArrayList<>(screenshots); // Return a copy to avoid modification
    }

    public static void clearScreenshots() {
        for (Bitmap bitmap : screenshots) {
            bitmap.recycle(); // Help to free memory
        }
        screenshots.clear();
    }
}
