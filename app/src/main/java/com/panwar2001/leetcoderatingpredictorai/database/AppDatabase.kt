package com.panwar2001.leetcoderatingpredictorai.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ContestEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contestDao(): ContestDao
}