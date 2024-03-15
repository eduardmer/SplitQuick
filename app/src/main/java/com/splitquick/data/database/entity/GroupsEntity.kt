package com.splitquick.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitquick.domain.model.Currency

@Entity
data class GroupsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val currency: Currency,
    val date: Long
)
