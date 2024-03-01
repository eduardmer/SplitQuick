package com.splitquick.domain.model

data class Group(
    val id: Long = 0,
    val name: String,
    val date: Long,
    val totalExpenses: Double = 0.0
) {

    override fun toString(): String {
        return name
    }

}
