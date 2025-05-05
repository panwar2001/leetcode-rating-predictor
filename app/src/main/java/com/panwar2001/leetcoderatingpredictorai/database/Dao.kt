package com.panwar2001.leetcoderatingpredictorai.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContest(contest : ContestEntity): Long

    @Query("select * from weeklycontest order by time desc;")
    fun retrieveAllContest(): Flow<List<ContestEntity>>

    @Query("delete from weeklycontest;")
    suspend fun deleteAllContest()

    @Query("SELECT COUNT(*) FROM weeklycontest")
    suspend fun countRows(): Int
}