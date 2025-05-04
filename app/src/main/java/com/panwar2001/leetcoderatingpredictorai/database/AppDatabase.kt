package com.panwar2001.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.panwar2001.leetcoderatingpredictorai.database.ContestDao
import com.panwar2001.leetcoderatingpredictorai.database.ContestEntity

@Database(entities = [ContestEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contestDao(): ContestDao
}