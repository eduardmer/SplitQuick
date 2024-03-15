package com.splitquick.domain.model

data class Settings(
    val fullName: String = "",
    val email: String = "",
    val isDarkModeEnabled: Boolean = false,
    val language: String = "",
    val version: String = ""
)
