package com.splitquick.data

import com.splitquick.BuildConfig
import com.splitquick.data.database.dao.ActivityDao
import com.splitquick.data.database.dao.ExpensesDao
import com.splitquick.data.database.dao.GroupsDao
import com.splitquick.data.database.dao.MembersDao
import com.splitquick.data.database.mapper.generateEvent
import com.splitquick.data.database.mapper.toDataModel
import com.splitquick.data.database.mapper.toDomainModel
import com.splitquick.data.datastore.PreferencesDataSource
import com.splitquick.data.datastore.ProtoDataSource
import com.splitquick.data.datastore.mapper.toDomainModel
import com.splitquick.domain.Repository
import com.splitquick.domain.model.Currency
import com.splitquick.domain.model.Event
import com.splitquick.domain.model.Expense
import com.splitquick.domain.model.Group
import com.splitquick.domain.model.Member
import com.splitquick.domain.model.Payment
import com.splitquick.domain.model.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import java.math.BigDecimal
import java.math.MathContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val preferencesDataSource: PreferencesDataSource,
    private val protoDataSource: ProtoDataSource,
    private val groupsDao: GroupsDao,
    private val membersDao: MembersDao,
    private val activityDao: ActivityDao,
    private val expensesDao: ExpensesDao
) : Repository {

    override val isDarkModeEnabled: Flow<Boolean> = preferencesDataSource.isDarkModeEnabled

    override val user = protoDataSource.user.map {
        it.toDomainModel()
    }

    override fun getGroupsByNameWithExpenses(group: String): Flow<List<Group>> {
        return groupsDao.getGroupsByName(group).map { items ->
            items.map {
                it.toDomainModel(expensesDao.getTotalExpensesForGroup(it.id) ?: BigDecimal.ZERO)
            }
        }
    }

    override fun getGroupsByName(group: String): Flow<List<Group>> {
        val groups = if (group.isEmpty())
            groupsDao.getAllGroups()
        else groupsDao.getGroupsByName(group)
        return groups.map { items ->
            items.map {
                it.toDomainModel()
            }
        }
    }

    override suspend fun saveUser(firstName: String, lastName: String, email: String) {
        protoDataSource.saveUser(firstName, lastName, email)
    }

    override fun addGroup(group: Group, members: List<Member>) = flow {
        val groupId = groupsDao.addGroup(group.toDataModel())
        membersDao.saveMembers(members.map { member ->
            member.toDataModel(groupId)
        })
        activityDao.addEvent(group.generateEvent())
        activityDao.addEvents(members.map {
            it.generateEvent(group.name)
        })
        emit(true)
    }

    override fun getMembersByGroup(groupId: Long): Flow<List<Member>> {
        return membersDao.getMembersByGroup(groupId).map { items ->
            items.map {
                it.toDomainModel()
            }
        }
    }

    override fun filterEvents(filter: String): Flow<List<Event>> {
        return activityDao.filterEvents(filter).map { items ->
            items.map {
                it.toDomainModel()
            }
        }
    }

    override suspend fun addExpense(expense: Expense) {
        expensesDao.addExpense(expense.toDataModel())
        activityDao.addEvent(expense.generateEvent())
    }

    override fun getAllExpensesForGroup(groupId: Long): Flow<List<Expense>> {
        return expensesDao.getAllExpensesForGroup(groupId).map { items ->
            items.map {
                it.toDomainModel()
            }
        }
    }

    override fun getCalculations(groupId: Long) = membersDao.getMembersByGroup(groupId).zip(expensesDao.getAllExpensesForGroup(groupId)) { members, expenses ->
        val currency = expenses.firstOrNull()?.currency ?: Currency.EUR
        val average = expenses.sumOf { it.amount }.divide(members.size.toBigDecimal(), MathContext.DECIMAL32)
        val items = mutableListOf<Payment>()
        members.forEach { member ->
            val allExpensesForMember = expenses.filter { it.contributorId == member.id }.sumOf { it.amount }
            items.add(Payment(member.id, "${member.firstName} ${member.lastName}", allExpensesForMember.minus(average), currency))
        }
        items
    }

    override val settings: Flow<Settings> = combine(
        protoDataSource.user,
        preferencesDataSource.isDarkModeEnabled,
        preferencesDataSource.language
    ) { user, isDarkModeEnabled, language ->
        Settings("${user.firstName} ${user.lastName}", user.email, isDarkModeEnabled, language, "v${BuildConfig.VERSION_NAME}/${BuildConfig.VERSION_CODE}")
    }

    override suspend fun enableDarkMode(isDarkModeEnabled: Boolean) {
        preferencesDataSource.setDarkMode(isDarkModeEnabled)
    }

    override suspend fun setLanguage(language: String) {
        preferencesDataSource.setLanguage(language)
    }

}