package com.panwar2001.leetcoderatingpredictorai.di

import com.panwar2001.leetcoderatingpredictorai.core.DataRepoInterface
import com.panwar2001.leetcoderatingpredictorai.core.DataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract  class  RepoModule {
    @Singleton
    @Binds
    abstract fun bindAppRepo(repository: DataRepository): DataRepoInterface
}
