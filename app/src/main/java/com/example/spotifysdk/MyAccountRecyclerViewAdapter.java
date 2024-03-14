package com.example.spotifysdk;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spotifysdk.placeholder.PlaceholderContent.PlaceholderItem;
import com.example.spotifysdk.databinding.FragmentAccountBinding;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAccountRecyclerViewAdapter extends RecyclerView.Adapter<MyAccountRecyclerViewAdapter.ViewHolder> {

    private final List<Object> mValues; // Change PlaceholderItem to Object

    public MyAccountRecyclerViewAdapter(List<Object> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Use your new layout for each item
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_account, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // You can bind data to your views here based on the position in the list
        // For simplicity, I'll just set the position as text
        holder.mIdView.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView eIdView;
        // Add more views for your EditText, TextViews, etc.

        public ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.Editusername); // Replace with your actual ID
            eIdView = view.findViewById(R.id.Editusername); // Replace with your actual ID

            // Initialize other views here
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }
}
