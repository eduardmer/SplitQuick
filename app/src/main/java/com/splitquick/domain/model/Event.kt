package com.splitquick.domain.model

data class Event(
    val id: Long,
    val memberName: String,
    val groupName: String,
    val expenseDescription: String = "",
    val amount: Double = 0.0,
    val action: EventType,
    val date: Long
)
