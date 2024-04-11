package com.example.spotifysdk.ui.gallery;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<List<IconDateItem>> iconItems = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<IconDateItem>> getIconItems() {
        return iconItems;
    }
    private MutableLiveData<Boolean> _imageClicked = new MutableLiveData<>();
    public LiveData<Boolean> imageClicked = _imageClicked;

    public void setImageClicked(boolean clicked) {
        _imageClicked.setValue(clicked);
    }

    public void addIconItem(IconDateItem item) {
        List<IconDateItem> currentItems = iconItems.getValue();
        if (currentItems == null) currentItems = new ArrayList<>();
        currentItems.add(item);
        iconItems.setValue(currentItems); // Trigger the observers
    }
}
