package com.example.spotifysdk.ui.gallery;

public class IconDateItem {
    private int iconResId;
    private String date;

    public IconDateItem(int iconResId, String date) {
        this.iconResId = iconResId;
        this.date = date;
    }

    // Getters
    public int getIconResId() {
        return iconResId;
    }

    public String getDate() {
        return date;
    }
}
