package com.splitquick.domain.model

data class Expense(
    val id: Long = 0,
    val contributorId: Long,
    val contributorName: String,
    val groupId: Long,
    val groupName: String,
    val description: String,
    val amount: Double,
    val date: Long
)
