package com.splitquick.data.database.mapper

import com.splitquick.data.database.entity.GroupsEntity
import com.splitquick.domain.model.Group
import java.math.BigDecimal

fun Group.toDataModel() = GroupsEntity(
    name = name,
    currency = currency,
    date = date
)

fun GroupsEntity.toDomainModel(totalExpenses: BigDecimal = BigDecimal.ZERO) = Group(
    id,
    name,
    date,
    currency,
    totalExpenses
)