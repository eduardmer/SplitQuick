package com.splitquick.domain.model

import java.math.BigDecimal

data class Expense(
    val id: Long = 0,
    val contributorId: Long,
    val contributorName: String,
    val groupId: Long,
    val groupName: String,
    val description: String,
    val amount: BigDecimal,
    val currency: Currency,
    val date: Long
)
