package com.panwar2001.leetcoderatingpredictorai.di

import android.content.Context
import androidx.room.Room
import com.panwar2001.leetcoderatingpredictorai.database.ContestDao
import com.panwar2001.leetcoderatingpredictorai.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "database.db"
        ).build()
    }

    @Provides
    fun provideTextFileDao(database: AppDatabase): ContestDao = database.contestDao()
}