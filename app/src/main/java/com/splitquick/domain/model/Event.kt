package com.splitquick.domain.model

import java.math.BigDecimal

data class Event(
    val id: Long,
    val memberName: String,
    val groupName: String,
    val expenseDescription: String = "",
    val amount: BigDecimal = BigDecimal.ZERO,
    val action: EventType,
    val date: Long
)
