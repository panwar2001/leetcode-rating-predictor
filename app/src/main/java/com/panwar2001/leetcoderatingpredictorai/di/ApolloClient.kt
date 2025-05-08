package com.panwar2001.leetcoderatingpredictorai.di

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApolloModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val okHttp = OkHttpClient
            .Builder()
            .addInterceptor(logging)
        return ApolloClient.Builder()
            .serverUrl("https://leetcode.com/graphql/")
            .okHttpClient(okHttp.build()) //ApolloClient with okhttp
            .build()
    }
}