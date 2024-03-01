package com.splitquick.domain

import com.splitquick.domain.model.Event
import com.splitquick.domain.model.Expense
import com.splitquick.domain.model.Group
import com.splitquick.domain.model.Member
import com.splitquick.domain.model.Payment
import com.splitquick.domain.model.Result
import com.splitquick.domain.model.User
import kotlinx.coroutines.flow.Flow

interface Repository {

    val user: Flow<User>

    fun getGroupsByNameWithExpenses(group: String): Flow<List<Group>>

    fun getGroupsByName(group: String): Flow<List<Group>>

    suspend fun saveUser(firstName: String, lastName: String, email: String)

    fun addGroup(group: Group, members: List<Member>): Flow<Boolean>

    fun getMembersByGroup(groupId: Long): Flow<List<Member>>

    fun filterEvents(filter: String): Flow<List<Event>>

    suspend fun addExpense(expense: Expense)

    fun getAllExpensesForGroup(groupId: Long): Flow<List<Expense>>

    fun getCalculations(groupId: Long): Flow<List<Payment>>

}