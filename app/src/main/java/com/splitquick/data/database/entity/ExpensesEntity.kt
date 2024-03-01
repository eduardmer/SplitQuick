package com.splitquick.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExpensesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val contributorId: Long,
    val contributorName: String,
    val groupId: Long,
    val groupName: String,
    val description: String,
    val amount: Double,
    val date: Long
)
