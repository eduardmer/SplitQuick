package com.splitquick.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val date: Long
)
