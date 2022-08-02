package com.example.realestatemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanager.R
import com.example.realestatemanager.data.viewmodel.CreateEditViewModel
import com.example.realestatemanager.databinding.FragmentCreateModifyBinding
import com.example.realestatemanager.domain.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CreateModifyFragment : Fragment() {

    private lateinit var binding: FragmentCreateModifyBinding
    lateinit var viewModel: CreateEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateModifyBinding.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.fragment_mortgage_calculator, container, false)
        viewModel = ViewModelProvider(requireActivity())[CreateEditViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPhoto.setOnClickListener {
            takePhoto.launch()
        }
    }

    private val takePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        val fileName = UUID.randomUUID().toString()
        val isSavedSuccessfully = viewModel.savePhotoToInternalStorage(fileName, it!!, requireContext())
        //if(isSavedSuccessfully) Utils.snackBarMaker(binding.root, "Photo saved successfully") else Utils.snackBarMaker(binding.root, "Error saving photo")
        val test = viewModel.getPhotoPath(requireContext(), fileName)
        Utils.snackBarMaker(binding.root, test)

    }
}