package com.splitquick.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.splitquick.data.database.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MembersDao {

    @Query("SELECT * FROM MemberEntity WHERE groupId = :groupId")
    fun getMembersByGroup(groupId: Long): Flow<List<MemberEntity>>

    @Insert
    suspend fun saveMembers(members: List<MemberEntity>)

}