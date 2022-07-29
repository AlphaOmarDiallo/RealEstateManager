package com.example.realestatemanager.data.repository.agent

import com.example.realestatemanager.data.localData.AgentDao
import com.example.realestatemanager.data.localData.LocalDatabase
import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.sampleData.SampleAgent
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class AgentRepositoryImpTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var localDatabase: LocalDatabase

    @Inject
    lateinit var agentDao: AgentDao

    private val agentList = SampleAgent.getSampleAgentList()

    private val agent1 = agentList[0]
    private val agent2 = agentList[1]
    private val agent3 = agentList[2]
    private val agent4 = agentList[3]


    @Before
    fun init() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        localDatabase.agentDao().nukeTable()
    }

    @Test
    fun assert_that_classes_are_injected_correctly() {
        assertThat(localDatabase).isNotNull()
        assertThat(agentDao).isNotNull()
    }

    @Test
    fun agent_are_added_correctly_in_database() = runTest {
        //Given

        var listAgentInDatabase: List<Agent>? = null
        agentDao.getListAllAgents().take(1).collect { listAgentInDatabase = it }
        val sizeListAgentInDatabase: Int = listAgentInDatabase!!.size
        assertThat(sizeListAgentInDatabase).isEqualTo(0)

        //When

        addAgentToDataBase()
        advanceUntilIdle()

        //Then

        var listAgentInDatabaseAfterInsertingAgent: List<Agent>? = null
        agentDao.getListAllAgents().take(1).collect { listAgentInDatabaseAfterInsertingAgent = it }
        val sizeListAgentInDatabaseAfterInsertingAgent: Int =
            listAgentInDatabaseAfterInsertingAgent!!.size

        assertThat(sizeListAgentInDatabase).isNotEqualTo(sizeListAgentInDatabaseAfterInsertingAgent)
        assertThat(sizeListAgentInDatabaseAfterInsertingAgent).isEqualTo(4)
    }

    @Test
    fun get_all_agents_from_database() = runTest {
        //Given

        val referenceListAgent = listOf(agent1, agent2, agent3, agent4)
        var listAgent: List<Agent>? = null

        //When
        addAgentToDataBase()
        agentDao.getListAllAgents().take(1).collect { listAgent = it }
        advanceUntilIdle()

        //Then
        assertThat(listAgent!!.size).isEqualTo(referenceListAgent.size)

        assertThat(referenceListAgent[0]).isEqualTo(listAgent!![0])
        assertThat(referenceListAgent[1]).isEqualTo(listAgent!![1])
        assertThat(referenceListAgent[2]).isEqualTo(listAgent!![2])
        assertThat(referenceListAgent[3]).isEqualTo(listAgent!![3])
    }

    @Test
    fun get_specific_agent_from_database() = runTest {
        // Given
        addAgentToDataBase()
        advanceUntilIdle()

        //When
        var agentCheck1: Agent? = null
        var agentCheck2: Agent? = null
        var agentCheck3: Agent? = null
        var agentCheck4: Agent? = null

        agentDao.getUserById(agent1.id).take(1).collect { agentCheck1 = it }
        agentDao.getUserById(agent2.id).take(1).collect { agentCheck2 = it }
        agentDao.getUserById(agent3.id).take(1).collect { agentCheck3 = it }
        agentDao.getUserById(agent4.id).take(1).collect { agentCheck4 = it }
        advanceUntilIdle()

        //Then
        assertThat(agent1).isEqualTo(agentCheck1)
        assertThat(agent2).isEqualTo(agentCheck2)
        assertThat(agent3).isEqualTo(agentCheck3)
        assertThat(agent4).isEqualTo(agentCheck4)
    }

    private suspend fun addAgentToDataBase() {
        localDatabase.agentDao().insertAgent(agent1)
        localDatabase.agentDao().insertAgent(agent2)
        localDatabase.agentDao().insertAgent(agent3)
        localDatabase.agentDao().insertAgent(agent4)
    }
}