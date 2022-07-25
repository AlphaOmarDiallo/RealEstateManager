package com.example.realestatemanager.data.repository.agent

import androidx.lifecycle.asLiveData
import com.example.realestatemanager.data.localData.AgentDao
import com.example.realestatemanager.data.localData.LocalDatabase
import com.example.realestatemanager.data.model.Agent
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runners.MethodSorters
import javax.inject.Inject

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AgentRepositoryImpTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val testDispatcher = UnconfinedTestDispatcher()
    private val scope = TestScope(testDispatcher)

    @Inject
    lateinit var localDatabase: LocalDatabase

    @Inject
    lateinit var agentDao: AgentDao

    private val agent1 = Agent(0, "john", "john@doe.com", null)
    private val agent2 = Agent(0, "johnny", "johnny@doe.com", null)
    private val agent3 = Agent(0, "johnas", "johnas@doe.com", null)
    private val agent4 = Agent(0, "jon", "jon@doe.com", null)


    @Before
    fun init() {
        hiltRule.inject()
        localDatabase.agentDao().nukeTable()
        Dispatchers.setMain(testDispatcher)
        addAgentToDataBase()
    }

    @After
    fun tearDown() {
        localDatabase.agentDao().nukeTable()
        Dispatchers.resetMain()
    }

    @Test
    fun a_assert_that_localDatabase_is_not_null() {
        assertThat(localDatabase).isNotNull()
    }

    @Test
    fun b_assert_that_agentDao_is_not_null() {
        assertThat(agentDao).isNotNull()
    }

    @Test
    fun c_agent_are_added_correctly_in_database() {
        var listAgent: List<Agent>? = null
        runBlocking { localDatabase.agentDao().getListAllAgents().collect{listAgent = it} }
        assertThat(listAgent).isNotNull()
        assertThat(listAgent!!.size).isEqualTo(4)
    }

    @Test
    fun d_getAllAgent() {
        val referenceListAgent = listOf(agent1, agent2, agent3, agent4)
        var listAgent: List<Agent>? = null

        runBlocking { listAgent = localDatabase.agentDao().getListAllAgents().asLiveData().value }
        assertThat(listAgent).isNotNull()
        assertThat(listAgent!!.size).isEqualTo(referenceListAgent.size)

        assertThat(referenceListAgent[0].name).isEqualTo(listAgent!![0].name)
        assertThat(referenceListAgent[1].name).isEqualTo(listAgent!![1].name)
        assertThat(referenceListAgent[2].name).isEqualTo(listAgent!![2].name)
        assertThat(referenceListAgent[3].name).isEqualTo(listAgent!![3].name)
    }

    fun addAgentToDataBase() {
        runBlocking { localDatabase.agentDao().insertAgent(agent1) }
        runBlocking { localDatabase.agentDao().insertAgent(agent2) }
        runBlocking { localDatabase.agentDao().insertAgent(agent3) }
        runBlocking { localDatabase.agentDao().insertAgent(agent4) }
    }

}