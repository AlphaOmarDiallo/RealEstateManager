package com.example.realestatemanager.data.repositories.agent

import com.example.realestatemanager.data.localData.AgentDao
import com.example.realestatemanager.data.model.Agent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AgentRepositoryImp @Inject constructor(
    private val agentDao: AgentDao
) : AgentRepository {

    val allAgent: Flow<List<Agent>> = agentDao.getListAllAgents()

    override suspend fun insertAgent(agent: Agent) {
        agentDao.insertAgent(agent)
    }

    override suspend fun updateAgent(agent: Agent) {
        agentDao.updateAgent(agent)
    }

    override suspend fun deleteAgent(agent: Agent) {
        agentDao.deleteAgent(agent)
    }
}