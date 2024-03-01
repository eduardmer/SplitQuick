package com.splitquick.data.database.mapper

import com.splitquick.data.database.entity.EventsEntity
import com.splitquick.domain.model.Event
import com.splitquick.domain.model.EventType
import com.splitquick.domain.model.Expense
import com.splitquick.domain.model.Group
import com.splitquick.domain.model.Member

fun EventsEntity.toDomainModel() = Event(
    id,
    memberName,
    groupName,
    expenseDescription,
    amount,
    action,
    date
)

fun Group.generateEvent() = EventsEntity(
    memberName = "",
    groupName = name,
    action = EventType.CreateGroupEvent,
    date = date
)

fun Member.generateEvent(groupName: String) = EventsEntity(
    memberName = "$firstName $lastName",
    groupName = groupName,
    action = EventType.AddMemberEvent,
    date = System.currentTimeMillis()
)

fun Expense.generateEvent() = EventsEntity(
    memberName = contributorName,
    groupName = groupName,
    expenseDescription = description,
    amount = amount,
    action = EventType.AddExpenseEvent,
    date = date
)