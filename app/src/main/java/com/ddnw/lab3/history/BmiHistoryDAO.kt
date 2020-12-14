package com.ddnw.lab3.history

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BmiHistoryDAO {
    @Query("SELECT * FROM bmihistoryentry ORDER BY bmihistoryentry.id DESC")
    suspend fun getAllSortByDate(): List<BmiHistoryEntry>

    @Insert
    suspend fun insert(entry: BmiHistoryEntry)

    @Delete
    suspend fun delete(entry: BmiHistoryEntry)
}