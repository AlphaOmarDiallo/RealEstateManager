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
import com.example.realestatemanager.databinding.PhotoItemBinding
import com.example.realestatemanager.ui.adapter.PhotoListAdapter.PhotoListViewHolder
import com.google.android.material.textfield.TextInputEditText

class PhotoListAdapter(
    diffCallback: DiffUtil.ItemCallback<String>,
    private val listener: PhotoListAdapter.OnItemPhotoListClickListener
) :
    ListAdapter<String, PhotoListViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListViewHolder {
        val binding = PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PhotoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ListDiff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.toString() == newItem.toString()
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }

    inner class PhotoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var loadPhoto: ImageView
        var description: TextInputEditText
        var saveOrAdd: Button

        fun bind(photo: String?) {
            description.visibility = View.GONE
            loadPhoto.load(photo)
        }

        init {
            loadPhoto = itemView.findViewById(R.id.ivLoadPhoto)
            description = itemView.findViewById(R.id.TIETDescription)
            saveOrAdd = itemView.findViewById(R.id.btnAddRemove)
            saveOrAdd.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) listener.onItemPhotoListClick(position)
        }
    }

    interface OnItemPhotoListClickListener {
        fun onItemPhotoListClick(position: Int)
    }

}