package com.example.hisaabkitaab.di

import android.content.Context
import com.example.hisaabkitaab.db.dao.FriendsDao
import com.example.hisaabkitaab.db.dao.MyExpenseDao
import com.example.hisaabkitaab.db.dao.TransactionDao
import com.example.hisaabkitaab.db.dao.UserDao
import com.example.hisaabkitaab.db.database.TransactionDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DiModule {

    @Provides
    @Singleton
    fun getDatabase(@ApplicationContext context: Context) : TransactionDatabase {
        return TransactionDatabase.getInstance(context)
    }

    @Provides
    fun getTransactionDao(database : TransactionDatabase) : TransactionDao {
        return database.getTransactionDao()
    }

    @Provides
    fun getFriendsDao(database: TransactionDatabase) : FriendsDao {
        return database.getFriendsDao()
    }

    @Provides
    fun getUserDao(database: TransactionDatabase) : UserDao {
        return database.getUserDao()
    }

    @Provides
    fun getMyExpenseDao(database: TransactionDatabase) : MyExpenseDao {
        return database.getMyExpenseDao()
    }
}