package com.example.realestatemanager.data.localData

import androidx.room.*
import com.example.realestatemanager.data.model.Agent
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAgent(agent: Agent)

    @Update
    suspend fun updateAgent(agent: Agent)

    @Delete
    suspend fun deleteAgent(agent: Agent)

    @Query("SELECT * FROM agent_table")
    fun getListAllAgents(): Flow<List<Agent>>

}