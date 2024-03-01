package com.splitquick.ui.model

data class Payments(
    val type: ItemType,
    val title: Int = -1,
    val contributorName: String = "",
    val description: String = "",
    val contributedAmount: Double = 0.0,
    val calculation: Double = 0.0
)
