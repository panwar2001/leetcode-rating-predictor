package com.panwar2001.leetcoderatingpredictorai.di

import com.panwar2001.leetcoderatingpredictorai.core.UserRepository
import com.panwar2001.leetcoderatingpredictorai.core.UserRepositoryImpl
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
    abstract fun bindAppRepo(repository: UserRepositoryImpl): UserRepository
}
