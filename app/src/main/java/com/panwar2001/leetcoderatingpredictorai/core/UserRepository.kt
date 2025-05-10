package com.panwar2001.leetcoderatingpredictorai.core

import android.content.Context
import android.util.Log
import android.util.Log.e
import com.apollographql.apollo.ApolloClient
import com.panwar2001.leetcoderatingpredictorai.di.LeetCodeApiService
import com.panwar2001.leetcoderatingpredictorai.inference.UserInput
import com.panwar2001.leetcoderatingpredictorai.inference.getUserInputArray
import com.panwar2001.leetcoderatingpredictorai.inference.runModel
import com.panwar2001.leetcoderatingpredictorai.inference.scaleInputData
import com.panwar2001.prediction.GetUserProfileQuery
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


interface UserRepository {
    suspend fun getUserProfile(username: String): Result<GetUserProfileQuery.Data>
    suspend fun predictUserRating(
        userRating: Float,
        contestAttended: Float,
        userRank: Float,
        contestTitle: String
    ): Float
}


@Singleton
class UserRepositoryImpl @Inject
constructor(
    private val apolloClient: ApolloClient,
    @ApplicationContext private val context: Context,
    private val apiService: LeetCodeApiService
): UserRepository {
    override suspend fun getUserProfile(username: String): Result<GetUserProfileQuery.Data> {
        return try {
            e("username",username)
            if(username==""){
                throw Exception("Empty user name")
            }
            val response = apolloClient.query(GetUserProfileQuery(username)).execute()
            e("response",response.toString())
            if(response.hasErrors() || response.errors !=null){
                throw Exception("response has errors")
            }
            val data = response.data
            if (data != null) {
                Result.success(data)
            } else {
                throw Exception("No data received")
            }
        } catch (error: Exception) {
            e("error",error.toString())
            Result.failure(error)
        }
    }

    override suspend fun predictUserRating(
        userRating: Float,
        contestAttended: Float,
        userRank: Float,
        contestTitle: String
    ): Float {
        try {
            val title = contestTitle.lowercase().replace(" ", "-")
            val totalParticipants = apiService.getContestRanking(title)
            val input = UserInput(
                userRating = userRating,
                contestAttended = contestAttended,
                userRank = userRank,
                totalParticipants = totalParticipants.userNum * 1f
            )
            Log.i("userRating", userRating.toString())
            Log.i("contestAttended", contestAttended.toString())
            Log.i("userRank", userRank.toString())
            Log.i("totalParticipants", totalParticipants.toString())

            val rawInput = getUserInputArray(input)

            val minValues = floatArrayOf(1195.253f, 0f, 500f, 0f, 0f)
            val maxValues = floatArrayOf(3104.812f, 25169f, 30506f, 108.78220141f, 185f)

            val scaledInput = scaleInputData(rawInput, minValues, maxValues)

            return runModel(context, scaledInput)
        }catch (error: Error){
            e("ERROR",error.message.toString())
            return 0f
        }
    }
}
