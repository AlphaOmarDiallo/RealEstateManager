package com.example.realestatemanager.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_table")
data class Agent(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "agent_id")
    val id: Int,
    @ColumnInfo(name = "agent_name")
    var name: String,
    @ColumnInfo(name = "agent_email")
    val email: String,
    @ColumnInfo(name = "agent_picture")
    val picture: String?
)
