package com.example.realestatemanager.di

import com.example.realestatemanager.data.remoteData.RetrofitGoogleAPI
import com.example.realestatemanager.data.repository.autocomplete.AutocompleteRepository
import com.example.realestatemanager.data.repository.autocomplete.AutocompleteRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AutocompleteModule {

    @Singleton
    @Provides
    fun provideAutocompleteRepository(retrofitGoogleAPI: RetrofitGoogleAPI): AutocompleteRepository =
        AutocompleteRepositoryImp(retrofitGoogleAPI)
}