package com.splitquick.domain.model

import com.splitquick.R

enum class EventType(val event: Int) {
    CreateGroupEvent(R.string.created_group),
    AddMemberEvent(R.string.added_member),
    AddExpenseEvent(R.string.added_expense)
}