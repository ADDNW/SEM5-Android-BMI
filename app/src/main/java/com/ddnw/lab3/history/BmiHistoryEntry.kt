package com.ddnw.lab3.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BmiHistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "bmi") val bmi: String,
    @ColumnInfo(name = "mass") val mass: String,
    @ColumnInfo(name = "height") val height: String,
    @ColumnInfo(name = "date") val date: String
)