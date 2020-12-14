package com.ddnw.lab3.history

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = [BmiHistoryEntry::class], version = 1)
abstract class BmiHistoryDatabase : RoomDatabase() {
    companion object {
        private var instance: BmiHistoryDatabase? = null
        private const val DATABASE_NAME = "BmiHistory"

        fun getInstance(context: Context): BmiHistoryDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    BmiHistoryDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
            return instance as BmiHistoryDatabase
        }
    }

    abstract fun historyDao(): BmiHistoryDAO
}
