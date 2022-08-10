package com.example.realestatemanager.ui.createEdit.adapter

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
import com.example.realestatemanager.data.model.media.SharedStoragePhoto
import com.example.realestatemanager.databinding.PhotoItemBinding
import com.google.android.material.textfield.TextInputEditText

class ExternalStorageAdapter(
    diffCallback: DiffUtil.ItemCallback<SharedStoragePhoto>,
    private val listener: OnItemExternalStoragePhotoClickListener
) :
    ListAdapter<SharedStoragePhoto, ExternalStorageAdapter.ExternalPhotoViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExternalPhotoViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExternalPhotoViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ExternalPhotoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ListDiff : DiffUtil.ItemCallback<SharedStoragePhoto>() {
        override fun areItemsTheSame(
            oldItem: SharedStoragePhoto,
            newItem: SharedStoragePhoto
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SharedStoragePhoto,
            newItem: SharedStoragePhoto
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.contentUri == newItem.contentUri
        }

    }

    inner class ExternalPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var loadPhoto: ImageView
        var description: TextInputEditText
        var saveOrAdd: Button

        fun bind(photo: SharedStoragePhoto?) {
            description.setText(photo!!.name)
            loadPhoto.load(photo.contentUri)
        }

        init {
            loadPhoto = itemView.findViewById(R.id.ivLoadPhoto)
            description = itemView.findViewById(R.id.TIETDescription)
            saveOrAdd = itemView.findViewById(R.id.btnAddRemove)
            saveOrAdd.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) listener.onItemExternalClick(position)
        }
    }

    interface OnItemExternalStoragePhotoClickListener {
        fun onItemExternalClick(position: Int)
    }

}