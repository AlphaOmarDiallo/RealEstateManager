package com.example.realestatemanager.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.realestatemanager.R
import com.example.realestatemanager.data.model.InternalStoragePhoto
import com.example.realestatemanager.databinding.PhotoItemBinding
import com.example.realestatemanager.ui.adapter.PhotoAdapter.PhotoViewHolder
import com.google.android.material.textfield.TextInputEditText

class PhotoAdapter(diffCallback: DiffUtil.ItemCallback<InternalStoragePhoto>) :
    ListAdapter<InternalStoragePhoto, PhotoViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ListDiff : DiffUtil.ItemCallback<InternalStoragePhoto>() {
        override fun areItemsTheSame(
            oldItem: InternalStoragePhoto,
            newItem: InternalStoragePhoto
        ): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: InternalStoragePhoto,
            newItem: InternalStoragePhoto
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var loadPhoto: ImageView
        var description: TextInputEditText
        var saveOrAdd: Button
        fun bind(photo: InternalStoragePhoto?) {
            description.setText(photo!!.name)
            loadPhoto.load(photo.bmp)
        }

        init {
            loadPhoto = itemView.findViewById(R.id.ivLoadPhoto)
            description = itemView.findViewById(R.id.TIETDescription)
            saveOrAdd = itemView.findViewById(R.id.btnAddRemove)
        }
    }
}