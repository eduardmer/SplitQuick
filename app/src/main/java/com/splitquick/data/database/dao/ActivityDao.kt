package com.splitquick.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.splitquick.data.database.entity.EventsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Query("SELECT * FROM EventsEntity ORDER BY date DESC")
    fun getAllActivities(): Flow<List<EventsEntity>>

    @Query("SELECT * FROM EventsEntity WHERE " +
            "memberName LIKE '%' || :filter || '%' OR " +
            "groupName LIKE '%' || :filter || '%' OR " +
            "expenseDescription LIKE '%' || :filter || '%'" +
            "ORDER BY date DESC")
    fun filterEvents(filter: String): Flow<List<EventsEntity>>

    @Insert
    suspend fun addEvent(event: EventsEntity)

    @Insert
    suspend fun addEvents(events: List<EventsEntity>)

}