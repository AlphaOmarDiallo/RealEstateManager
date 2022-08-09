package com.example.realestatemanager.di

import com.example.realestatemanager.data.repository.permission.PermissionRepository
import com.example.realestatemanager.data.repository.permission.PermissionRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PermissionModule {

    @Singleton
    @Provides
    fun providePermissionRepository(): PermissionRepository = PermissionRepositoryImp()
}