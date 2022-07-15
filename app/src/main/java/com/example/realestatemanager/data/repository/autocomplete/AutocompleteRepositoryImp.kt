package com.example.realestatemanager.data.repository.autocomplete

import com.example.realestatemanager.data.model.autocomplete.Autocomplete
import com.example.realestatemanager.data.remoteData.RetrofitGoogleAPI
import com.example.realestatemanager.domain.Constant
import retrofit2.Response
import javax.inject.Inject

class AutocompleteRepositoryImp @Inject constructor(
    private val retrofitGoogleAPI: RetrofitGoogleAPI
): AutocompleteRepository {

    override suspend fun autocompleteAddress(input: String): Response<Autocomplete> {
        return retrofitGoogleAPI.autocompleteAddress(
            input,
            Constant.AUTOCOMPLETE_COMPONENTS,
            Constant.AUTOCOMPLETE_OFFSET,
            Constant.AUTOCOMPLETE_TYPES,
            Constant.GOOGLE_API_KEY
        )
    }
}