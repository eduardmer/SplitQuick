package com.splitquick.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.splitquick.data.database.dao.ActivityDao
import com.splitquick.data.database.dao.ExpensesDao
import com.splitquick.data.database.dao.GroupsDao
import com.splitquick.data.database.dao.MembersDao
import com.splitquick.data.database.entity.EventsEntity
import com.splitquick.data.database.entity.ExpensesEntity
import com.splitquick.data.database.entity.GroupsEntity
import com.splitquick.data.database.entity.MemberEntity

@Database(entities = [GroupsEntity::class, MemberEntity::class, EventsEntity::class, ExpensesEntity::class], version = 1)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun groupsDao(): GroupsDao

    abstract fun membersDao(): MembersDao

    abstract fun activityDao(): ActivityDao

    abstract fun expensesDao(): ExpensesDao

}