package com.panwar2001.leetcoderatingpredictorai.di

import com.google.gson.annotations.SerializedName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.jvm.java

data class ContestRankingResponse(
    @SerializedName("user_num")
    val userNum: Int
)

interface LeetCodeApiService {
    @GET("contest/api/ranking/{contest_title}/")
    suspend fun getContestRanking(@Path("contest_title") contestTitle: String): ContestRankingResponse
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://leetcode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLeetCodeApiService(retrofit: Retrofit): LeetCodeApiService {
        return retrofit.create(LeetCodeApiService::class.java)
    }
}

