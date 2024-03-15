package com.splitquick.domain.model

import java.math.BigDecimal

data class Group(
    val id: Long = 0,
    val name: String = "",
    val date: Long = 0,
    val currency: Currency = Currency.USD,
    val totalExpenses: BigDecimal = BigDecimal.ZERO
) {

    override fun toString(): String {
        return name
    }

}
