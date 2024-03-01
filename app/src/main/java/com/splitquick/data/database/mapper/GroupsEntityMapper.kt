package com.splitquick.data.database.mapper

import com.splitquick.data.database.entity.GroupsEntity
import com.splitquick.domain.model.Group

fun Group.toDataModel() = GroupsEntity(
    name = name,
    date = date
)

fun GroupsEntity.toDomainModel(totalExpenses: Double = 0.0) = Group(
    id,
    name,
    date,
    totalExpenses
)