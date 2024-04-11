package com.example.spotifysdk.ui.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifysdk.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private final List<GalleryItem> galleryItems;
    private final LayoutInflater inflater;

    public GalleryAdapter(Context context, List<GalleryItem> galleryItems) {
        this.galleryItems = galleryItems;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.gallery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GalleryItem item = galleryItems.get(position);
        holder.iconImageView.setImageResource(item.getIconResourceId());
        holder.dateTextView.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
