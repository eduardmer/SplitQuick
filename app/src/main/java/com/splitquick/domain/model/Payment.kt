package com.splitquick.domain.model

import java.math.BigDecimal

data class Payment(
    val giverId: Long,
    val giverName: String,
    val amount: BigDecimal,
    val currency: Currency
)
