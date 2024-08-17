package com.route.todoappc40gsunwed.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

// Table
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    val title: String? = null,
    val description: String? = null,
    val date: Date? = null,
    val time: String? = null,
    val isDone: Boolean? = null
)

