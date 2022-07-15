package com.example.realestatemanager.data.repository.autocomplete

import com.example.realestatemanager.data.model.autocomplete.Autocomplete
import retrofit2.Response

interface AutocompleteRepository {

    suspend fun autocompleteAddress(input: String): Response<Autocomplete>

}