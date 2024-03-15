package com.splitquick.ui.model

import com.splitquick.domain.model.Group
import com.splitquick.domain.model.User

data class GroupsScreenData(
    val user: User,
    val groups: List<Group>
)
