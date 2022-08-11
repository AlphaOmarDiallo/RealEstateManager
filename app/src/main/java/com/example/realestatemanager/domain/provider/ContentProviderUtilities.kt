package com.example.realestatemanager.domain.provider

import com.example.realestatemanager.data.repository.agent.AgentRepository
import com.example.realestatemanager.data.repository.property.PropertyRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ContentProviderUtilities {
    var propertyRepository: PropertyRepository
    var agentRepository: AgentRepository
}