package com.example.spotifysdk.ui.gallery;

public class GalleryItem {
    private final int iconResourceId;
    private final String date;

    public GalleryItem(int iconResourceId, String date) {
        this.iconResourceId = iconResourceId;
        this.date = date;
    }

    // Getters
    public int getIconResourceId() { return iconResourceId; }
    public String getDate() { return date; }
}
