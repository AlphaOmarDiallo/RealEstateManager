package com.example.realestatemanager.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.data.model.Photo;

import java.util.List;

public class PhotoAdapter extends ListAdapter<Photo, PhotoAdapter.PhotoViewHolder> {

    private final List<Photo> listPhoto;

    protected PhotoAdapter(@NonNull DiffUtil.ItemCallback<Photo> diffCallback, List<Photo> listPhoto) {
        super(diffCallback);
        this.listPhoto = listPhoto;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {

    }

    public static class ListDiff extends DiffUtil.ItemCallback<Photo> {

        @Override
        public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.getUri().equals(newItem.getUri());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.getUri().equals(newItem.getUri()) && oldItem.getDescription().equals(newItem.getDescription());
        }
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(Photo photo) {

        }
    }
}
