package com.example.realestatemanager.data.repository.agent

import com.example.realestatemanager.data.model.Agent

interface AgentRepository {

    suspend fun insertAgent(agent: Agent)

    suspend fun updateAgent(agent: Agent)

    suspend fun deleteAgent(agent: Agent)
}