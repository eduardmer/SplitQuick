package com.splitquick.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.splitquick.data.database.entity.ExpensesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpensesDao {

    @Query("SELECT * FROM ExpensesEntity WHERE groupId = :groupId")
    fun getAllExpensesForGroup(groupId: Long): Flow<List<ExpensesEntity>>

    @Query("SELECT SUM(amount) AS value FROM ExpensesEntity WHERE groupId = :groupId")
    suspend fun getTotalExpensesForGroup(groupId: Long): Double?

    @Insert
    suspend fun addExpense(expense: ExpensesEntity)

}