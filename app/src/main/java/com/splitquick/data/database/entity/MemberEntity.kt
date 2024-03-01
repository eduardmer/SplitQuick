package com.splitquick.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.splitquick.domain.model.Member

@Entity
data class MemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val groupId: Long
)
