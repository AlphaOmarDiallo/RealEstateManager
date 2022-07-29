@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.realestatemanager.data.localData

import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.model.Property
import com.example.realestatemanager.data.sampleData.SampleAgent
import com.example.realestatemanager.data.sampleData.SampleProperties
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
@HiltAndroidTest
class LocalDatabaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var localDatabase: LocalDatabase

    @Inject
    lateinit var agentDao: AgentDao

    @Inject
    lateinit var propertyDao: PropertyDao

    @Before
    fun init() {
        hiltRule.inject()
        localDatabase.agentDao().nukeTable()
        localDatabase.propertyDao().nukeTable()
    }

    @After
    fun close() {
        localDatabase.agentDao().nukeTable()
        localDatabase.propertyDao().nukeTable()
    }

    private var agent = SampleAgent.getSampleAgentList()[0]
    private var property = SampleProperties.samplePropertyList[0]

    @Test
    fun classes_injected_with_success() {
        assertThat(localDatabase).isNotNull()
        assertThat(agentDao).isNotNull()
        assertThat(propertyDao).isNotNull()
    }

    @Test
    fun create_update_delete_agent() = runTest {
        lateinit var listAgent: List<Agent>
        val flowList: Flow<List<Agent>> = localDatabase.agentDao().getListAllAgents()
        val newName = "Jane Doe"

        //Check list is empty before starting test
        listAgent = flowList.first().toList()
        advanceUntilIdle()

        assertThat(listAgent.isEmpty()).isTrue()

        //Adding agent to database
        localDatabase.agentDao().insertAgent(agent)
        listAgent = flowList.first().toList()
        advanceUntilIdle()

        assertThat(listAgent.isNotEmpty()).isTrue()
        assertThat(listAgent).contains(agent)

        //Updating agent
        agent.name = newName

        //Updating agent in database
        localDatabase.agentDao().updateAgent(agent)
        listAgent = flowList.first().toList()
        advanceUntilIdle()

        assertThat(listAgent.last().name == newName).isTrue()
        assertThat(listAgent.size == 1).isTrue()

        //Deleting agent
        localDatabase.agentDao().deleteAgent(listAgent.last())
        listAgent = flowList.first().toList()
        advanceUntilIdle()

        assertThat(listAgent.contains(agent)).isFalse()
    }

    @Test
    fun create_update_property() = runTest {
        lateinit var listProperty: List<Property>
        val flowList: Flow<List<Property>> = localDatabase.propertyDao().getListOfProperties()

        //Check list is empty before starting test
        listProperty = flowList.first().toList()
        advanceUntilIdle()
        assertThat(listProperty.isEmpty()).isTrue()

        //Adding agent to database
        localDatabase.propertyDao().insertProperty(property)
        listProperty = flowList.first().toList()
        advanceUntilIdle()

        assertThat(listProperty.isNotEmpty()).isTrue()
        assertThat(listProperty.last().address == property.address && listProperty.last().price == property.price).isTrue()

        //Updating agent
        val property1 = listProperty.last()
        property1.price = 48326340

        //Updating agent in database
        localDatabase.propertyDao().updateProperty(property1)
        listProperty = flowList.first().toList()
        advanceUntilIdle()

        assertThat(listProperty.last().price == 48326340 && listProperty.size == 1).isTrue()
    }

}