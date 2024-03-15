package com.splitquick.data.database.mapper

import com.splitquick.data.database.entity.ExpensesEntity
import com.splitquick.domain.model.Expense

fun Expense.toDataModel() = ExpensesEntity(
    contributorId = contributorId,
    contributorName = contributorName,
    groupId = groupId,
    groupName = groupName,
    description = description,
    amount = amount,
    currency = currency,
    date = date
)

fun ExpensesEntity.toDomainModel() = Expense(
    id = id,
    contributorId = contributorId,
    contributorName = contributorName,
    groupId = groupId,
    groupName = groupName,
    description = description,
    amount = amount,
    currency = currency,
    date = date
)