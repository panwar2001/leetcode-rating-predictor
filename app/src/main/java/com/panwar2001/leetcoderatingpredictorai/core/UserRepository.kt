package com.panwar2001.leetcoderatingpredictorai.core

import android.R.attr.data
import android.util.Log
import android.util.Log.e
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.panwar2001.prediction.GetUserProfileQuery
import java.lang.System.console
import javax.inject.Inject
import javax.inject.Singleton


interface UserRepository {
    suspend fun getUserProfile(username: String): Result<GetUserProfileQuery.Data>
    fun predictUserRating(data: GetUserProfileQuery.Data): Float
}


@Singleton
class UserRepositoryImpl @Inject
constructor(
    private val apolloClient: ApolloClient
): UserRepository {
    override suspend fun getUserProfile(username: String): Result<GetUserProfileQuery.Data> {
        return try {
            e("username",username)
            if(username==""){
                throw Exception("Empty user name")
            }
            val user = Optional.presentIfNotNull(username)
            val response = apolloClient.query(GetUserProfileQuery(user)).execute()
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

    override fun predictUserRating(data: GetUserProfileQuery.Data): Float {
        return 0f
    }
}
