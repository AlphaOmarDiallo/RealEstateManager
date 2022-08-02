package com.example.realestatemanager.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.realestatemanager.data.repository.media.MediaStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateEditViewModel @Inject constructor(
    private val mediaStoreRepository: MediaStoreRepository
): ViewModel() {
}