package com.example.realestatemanager.data.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent_table")
data class Agent(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "agent_id")
    val id: Int = 0,
    @ColumnInfo(name = "agent_name")
    val name: String,
    @ColumnInfo(name = "agent_picture")
    val picture: Bitmap?
)
