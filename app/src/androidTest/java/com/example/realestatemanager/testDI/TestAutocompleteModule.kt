package com.example.realestatemanager.testDI

import com.example.realestatemanager.data.remoteData.RetrofitGoogleAPI
import com.example.realestatemanager.data.repository.autocomplete.AutocompleteRepository
import com.example.realestatemanager.data.repository.autocomplete.AutocompleteRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AutocompleteModule::class]
)
class TestAutocompleteModule {

    @Singleton
    @Provides
    fun provideAutocompleteRepository(retrofitGoogleAPI: RetrofitGoogleAPI): AutocompleteRepository =
        AutocompleteRepositoryImp(retrofitGoogleAPI)
}