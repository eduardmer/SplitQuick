package com.splitquick.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitquick.domain.model.EventType

@Entity
data class EventsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val memberName: String,
    val groupName: String,
    val expenseDescription: String = "",
    val amount: Double = 0.0,
    val action: EventType,
    val date: Long
)
