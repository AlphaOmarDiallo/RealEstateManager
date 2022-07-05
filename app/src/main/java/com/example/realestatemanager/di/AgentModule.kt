package com.example.realestatemanager.di

import com.example.realestatemanager.data.localData.AgentDao
import com.example.realestatemanager.data.repositories.agent.AgentRepository
import com.example.realestatemanager.data.repositories.agent.AgentRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AgentModule {

    @Provides
    fun provideAgentRepository(agentDao: AgentDao): AgentRepository = AgentRepositoryImp(agentDao)
}