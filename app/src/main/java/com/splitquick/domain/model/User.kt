package com.splitquick.domain.model

data class User(
    val firstName: String,
    val lastName: String,
    val email: String
) {

    fun isEmpty(): Boolean {
        return firstName.isEmpty() && lastName.isEmpty() && email.isEmpty()
    }

}
