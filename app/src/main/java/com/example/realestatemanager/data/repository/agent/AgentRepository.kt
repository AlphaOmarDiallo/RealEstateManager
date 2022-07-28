package com.example.realestatemanager.data.repository.agent

import com.example.realestatemanager.data.model.Agent
import kotlinx.coroutines.flow.Flow

interface AgentRepository {

    suspend fun insertAgent(agent: Agent)

    suspend fun updateAgent(agent: Agent)

    suspend fun deleteAgent(agent: Agent)

    fun getAllAgent(): Flow<List<Agent>>

    fun getUserById(id: String) : Agent?
}