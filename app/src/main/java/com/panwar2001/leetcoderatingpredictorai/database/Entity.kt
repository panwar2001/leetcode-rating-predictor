package com.panwar2001.leetcoderatingpredictorai.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WeeklyContest")
data class ContestEntity(
    @PrimaryKey val contestName: String
)