package com.splitquick.data.database.mapper

import com.splitquick.data.database.entity.MemberEntity
import com.splitquick.domain.model.Member

fun MemberEntity.toDomainModel() = Member(
    id,
    firstName,
    lastName
)

fun Member.toDataModel(groupId: Long) = MemberEntity(
    groupId = groupId,
    firstName = firstName,
    lastName = lastName,
)