package com.example.realestatemanager.data.localData

import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.model.Property
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runners.MethodSorters
import java.util.*
import javax.inject.Inject

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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

    private var agent = Agent(0, "John Doe", "johnDoe@test.com", null)
    private var property = Property(
        0, "apartment", 3000000, 300, 10, "lorem ipsum....", null,
        "somewhere", null, "In sale", Calendar.getInstance().timeInMillis, null, null
    )

    @Test
    fun a_localDataBase_injected_with_success() {
        assertThat(localDatabase).isNotNull()
    }

    @Test
    fun b_agentDao_injected_with_success() {
        assertThat(agentDao).isNotNull()
    }

    @Test
    fun c_propertyDao_injected_with_success() {
        assertThat(propertyDao).isNotNull()
    }

    @Test
    fun d_create_update_delete_agent() {
        lateinit var listAgent: List<Agent>
        val flowList: Flow<List<Agent>> = localDatabase.agentDao().getListAllAgents()

        //Check list is empty before starting test
        runBlocking { listAgent = flowList.first().toList() }
        assertThat(listAgent.isEmpty()).isTrue()

        //Adding agent to database
        runBlocking { localDatabase.agentDao().insertAgent(agent) }
        runBlocking { listAgent = flowList.first().toList() }
        assertThat(listAgent.isNotEmpty()).isTrue()
        assertThat(listAgent.last().name == agent.name && listAgent.last().email == agent.email).isTrue()

        //Updating agent
        var agent2 = listAgent.last()
        agent2.name = "Jane Doe"

        //Updating agent in database
        runBlocking { localDatabase.agentDao().updateAgent(agent2) }
        runBlocking { listAgent = flowList.first().toList() }
        assertThat(listAgent.last().name == "Jane Doe" && listAgent.size == 1).isTrue()

        //Deleting agent
        runBlocking { localDatabase.agentDao().deleteAgent(listAgent.last()) }
        runBlocking { listAgent = flowList.first().toList() }
        assertThat(listAgent.contains(agent2)).isFalse()
    }

    @Test
    fun e_create_update_property() {
        lateinit var listProperty: List<Property>
        val flowList: Flow<List<Property>> = localDatabase.propertyDao().getListOfProperties()

        //Check list is empty before starting test
        runBlocking { listProperty = flowList.first().toList() }
        assertThat(listProperty.isEmpty()).isTrue()

        //Adding agent to database
        runBlocking { localDatabase.propertyDao().insertProperty(property) }
        runBlocking { listProperty = flowList.first().toList() }
        assertThat(listProperty.isNotEmpty()).isTrue()
        assertThat(listProperty.last().address == property.address && listProperty.last().price == property.price).isTrue()

        //Updating agent
        var property1 = listProperty.last()
        property1.price = 48326340

        //Updating agent in database
        runBlocking { localDatabase.propertyDao().updateProperty(property1) }
        runBlocking { listProperty = flowList.first().toList() }
        assertThat(listProperty.last().price == 48326340 && listProperty.size == 1).isTrue()
    }




}