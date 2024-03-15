package com.splitquick.ui.model

import com.splitquick.domain.model.Currency
import java.math.BigDecimal

data class Payments(
    val type: ItemType,
    val title: Int = -1,
    val contributorName: String = "",
    val description: String = "",
    val contributedAmount: BigDecimal = BigDecimal.ZERO,
    val calculation: BigDecimal = BigDecimal.ZERO,
    val currency: Currency = Currency.EUR
)
