package com.splitquick.data.datastore.mapper

import com.splitquick.UserPreferences
import com.splitquick.domain.model.User

fun UserPreferences.toDomainModel() = User(
    firstName,
    lastName,
    email
)