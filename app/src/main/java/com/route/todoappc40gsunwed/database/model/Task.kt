package com.route.todoappc40gsunwed.database.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

// Table
@Parcelize
@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    var title: String? = null,
    var description: String? = null,
    var date: Date? = null,
    var time: String? = null,
    var isDone: Boolean? = null
):Parcelable

