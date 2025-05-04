package com.panwar2001.leetcoderatingpredictorai.core

import android.util.Log.e
import com.apollographql.apollo.ApolloClient
import com.panwar2001.leetcoderatingpredictorai.GetContestHistoryQuery
import com.panwar2001.leetcoderatingpredictorai.database.ContestDao
import com.panwar2001.leetcoderatingpredictorai.database.ContestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


interface DataRepoInterface {
    suspend fun loadNewContestData()
    suspend fun loadContestDataIfEmptyDB()
}

@Singleton
class DataRepository @Inject
constructor(
    private val contestDao: ContestDao
): DataRepoInterface {

    override suspend fun loadNewContestData() {
//        try {
//            withContext(Dispatchers.IO) {
//                var client  = ApolloClient.Builder()
//                    .serverUrl("https://leetcode.com/graphql/")
//                    .build()
//                contestDao.deleteAllContest()
//                val response = client.query(GetContestHistoryQuery(username = "loft_plyr")).execute()
//                val history= response.data?.userContestRankingHistory
//                if (response.data != null && history!=null) {
//                    history.forEach {
//                        contestDao.insertContest(contest = ContestEntity(it?.contest?.title.toString()))
//                    }
//                } else {
//                    e("error", "unable to fetch data")
////                    // Something wrong happened
////                    if (response.exception != null) {
////                        // Handle fetch errors
////                    } else {
////                        // Handle GraphQL errors in response.errors
////                    }
//                }
//            }
//        }catch (e: Exception){
//            e("error",e.message.toString())
//        }
    }

    override suspend fun loadContestDataIfEmptyDB() {
        if(contestDao.countRows()==0){
            loadNewContestData()
        }
    }
}
