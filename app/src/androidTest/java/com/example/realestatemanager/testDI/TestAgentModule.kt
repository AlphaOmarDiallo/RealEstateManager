package com.example.realestatemanager.testDI

import com.example.realestatemanager.data.localData.AgentDao
import com.example.realestatemanager.data.repositories.agent.AgentRepository
import com.example.realestatemanager.data.repositories.agent.AgentRepositoryImp
import com.example.realestatemanager.di.AgentModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AgentModule::class]
)
class TestAgentModule {

    @Provides
    fun provideAgentRepository(agentDao: AgentDao): AgentRepository = AgentRepositoryImp(agentDao)
}