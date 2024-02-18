package com.example.hisaabkitaab.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.room.util.query
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.impl.Migration_1_2
import com.example.hisaabkitaab.db.dao.FriendsDao
import com.example.hisaabkitaab.db.dao.MyExpenseDao
import com.example.hisaabkitaab.db.dao.TransactionDao
import com.example.hisaabkitaab.db.dao.UserDao
import com.example.hisaabkitaab.db.entity.Friend
import com.example.hisaabkitaab.db.entity.MyExpense
import com.example.hisaabkitaab.db.entity.Transaction
import com.example.hisaabkitaab.db.entity.User

@Database([Transaction::class, Friend::class, User::class, MyExpense::class], version = 2)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun getTransactionDao() : TransactionDao
    abstract fun getFriendsDao() : FriendsDao
    abstract fun getUserDao() : UserDao
    abstract fun getMyExpenseDao() : MyExpenseDao
    companion object {
        var instance : TransactionDatabase? = null
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE user_table ADD COLUMN lastUpdateDate INTEGER ")
            }
        }

        fun getInstance(context: Context) : TransactionDatabase {
            return if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    TransactionDatabase::class.java,
                    "Transaction_Database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                instance!!
            } else {
                instance!!
            }
        }
    }
}