package com.splitquick.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitquick.domain.model.EventType
import java.math.BigDecimal

@Entity
data class EventsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val memberName: String,
    val groupName: String,
    val expenseDescription: String = "",
    val amount: BigDecimal = BigDecimal.ZERO,
    val action: EventType,
    val date: Long
)
