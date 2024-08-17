package com.route.todoappc40gsunwed.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.route.todoappc40gsunwed.database.dao.TaskDao
import com.route.todoappc40gsunwed.database.model.Task
import com.route.todoappc40gsunwed.database.typeConverters.DateConverter

@Database([Task::class], version = 1) // 3   // Migration ->
@TypeConverters(DateConverter::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao

    companion object {
        // Singleton Design patterns  -> Design patterns -> Software
        private var INSTANCE: TaskDatabase? = null
        private const val DATABASE_NAME = "Tasks Database"
        fun getInstance(context: Context): TaskDatabase {
            if (INSTANCE == null)
                INSTANCE = Room.databaseBuilder(context, TaskDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration() // Migrations
                    .allowMainThreadQueries()         // Main Thread (UI Thread)-> Handle Navigation -
                    // Button Clicks - Handle Interaction with user  - Background Thread -> Coroutines
                    .build()
            return INSTANCE!!

        }

    }

}

