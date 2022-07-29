package com.example.realestatemanager.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_table")
data class Agent(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "agent_id")
    val id: String,
    @ColumnInfo(name = "agent_name")
    var name: String,
    @ColumnInfo(name = "agent_email")
    var email: String,
    @ColumnInfo(name = "agent_picture")
    var picture: String?
)
