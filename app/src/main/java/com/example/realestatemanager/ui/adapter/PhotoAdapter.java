package com.example.realestatemanager.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestatemanager.R;
import com.example.realestatemanager.data.model.InternalStoragePhoto;
import com.example.realestatemanager.databinding.PhotoItemBinding;
import com.google.android.material.textfield.TextInputEditText;

public class PhotoAdapter extends ListAdapter<InternalStoragePhoto, PhotoAdapter.PhotoViewHolder> {

    public PhotoAdapter(@NonNull DiffUtil.ItemCallback<InternalStoragePhoto> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PhotoItemBinding binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PhotoViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class ListDiff extends DiffUtil.ItemCallback<InternalStoragePhoto> {

        @Override
        public boolean areItemsTheSame(@NonNull InternalStoragePhoto oldItem, @NonNull InternalStoragePhoto newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull InternalStoragePhoto oldItem, @NonNull InternalStoragePhoto newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView loadPhoto;
        TextInputEditText description;
        Button saveOrAdd;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            loadPhoto = itemView.findViewById(R.id.ivLoadPhoto);
            description = itemView.findViewById(R.id.TIETDescription);
            saveOrAdd = itemView.findViewById(R.id.btnAddRemove);
        }

        public void bind(InternalStoragePhoto photo) {
            description.setText(photo.getName());
        }
    }
}
