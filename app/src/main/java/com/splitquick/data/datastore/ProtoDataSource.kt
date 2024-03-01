package com.splitquick.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.splitquick.UserPreferences
import com.splitquick.data.datastore.mapper.toDomainModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtoDataSource @Inject constructor(private val dataStore: DataStore<UserPreferences>) {

    val user = dataStore.data

    suspend fun saveUser(firstName: String, lastName: String, email: String) {
        dataStore.updateData {
            it.toBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .build()
        }
    }

}