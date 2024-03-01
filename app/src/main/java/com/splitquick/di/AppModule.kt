package com.splitquick.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.splitquick.UserPreferences
import com.splitquick.data.database.AppDatabase
import com.splitquick.data.database.dao.ActivityDao
import com.splitquick.data.database.dao.ExpensesDao
import com.splitquick.data.database.dao.GroupsDao
import com.splitquick.data.database.dao.MembersDao
import com.splitquick.data.datastore.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context,
        userPreferencesSerializer: UserPreferencesSerializer
    ): DataStore<UserPreferences> {
        return DataStoreFactory.create(serializer = userPreferencesSerializer) {
            context.dataStoreFile("user_preferences.proto")
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGroupsDao(database: AppDatabase): GroupsDao {
        return database.groupsDao()
    }

    @Provides
    @Singleton
    fun provideMembersDao(database: AppDatabase): MembersDao {
        return database.membersDao()
    }

    @Provides
    @Singleton
    fun provideActivityDao(database: AppDatabase): ActivityDao {
        return database.activityDao()
    }

    @Provides
    @Singleton
    fun provideExpensesDao(database: AppDatabase): ExpensesDao {
        return database.expensesDao()
    }

}