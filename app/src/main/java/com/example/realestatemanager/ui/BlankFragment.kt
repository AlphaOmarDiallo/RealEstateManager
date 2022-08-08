package com.example.realestatemanager.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.compose.AsyncImage
import com.example.realestatemanager.data.model.InternalStoragePhoto
import com.example.realestatemanager.data.viewmodel.PropertyDetailViewModel
import com.example.realestatemanager.domain.BitmapUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlankFragment : Fragment() {

    private val viewModel: PropertyDetailViewModel by viewModels()
    var listPhotoBitmap = listOf<InternalStoragePhoto>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getListInternalPhoto(requireContext())
        return ComposeView(requireContext()).apply {
            setContent {
                showImage()
            }
        }
    }

    @Composable
    fun showImage(){

        viewModel._listInternalStoragePhoto.observe(requireActivity()) { listPhotoBitmap = it }

        Box(modifier = Modifier.fillMaxSize()) {

            Log.e(TAG, "onCreateView: $listPhotoBitmap")
            if(listPhotoBitmap.isNotEmpty()){
                Log.i(TAG, "showImage: in here")
                val smallBMP = BitmapUtils.getResizedBitmap(listPhotoBitmap?.get(0)?.bmp!!, 1000)
                AsyncImage(model = smallBMP, contentDescription = "")
            }
        }
    }

}