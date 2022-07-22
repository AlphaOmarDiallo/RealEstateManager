package com.example.realestatemanager.data.localData

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.realestatemanager.data.model.Agent
import com.example.realestatemanager.data.model.Property

@Database(entities = [Property::class, Agent::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao

    abstract fun agentDao(): AgentDao
}