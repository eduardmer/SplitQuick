package com.splitquick.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.splitquick.data.database.entity.EventsEntity
import com.splitquick.data.database.entity.GroupsEntity
import com.splitquick.data.database.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupsDao {

    @Query("SELECT * FROM GroupsEntity")
    fun getAllGroups(): Flow<List<GroupsEntity>>

    @Query("SELECT * FROM GroupsEntity WHERE name LIKE '%' || :name || '%'")
    fun getGroupsByName(name: String): Flow<List<GroupsEntity>>

    @Insert
    suspend fun addGroup(group: GroupsEntity): Long

}