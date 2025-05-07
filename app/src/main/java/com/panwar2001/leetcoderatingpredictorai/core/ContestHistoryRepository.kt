package com.panwar2001.leetcoderatingpredictorai.core

import android.util.Log.e
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.panwar2001.leetcoderatingpredictorai.GetContestHistoryQuery
import com.panwar2001.leetcoderatingpredictorai.database.ContestDao
import com.panwar2001.leetcoderatingpredictorai.database.ContestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.forEach

interface ContestHistoryRepoInterface {
    suspend fun loadNewContestData( name: Optional<String?>)
    suspend fun loadContestDataIfEmptyDB()
}

class ContestHistoryRepository  @Inject
constructor(
    private val contestDao: ContestDao,
    private val client: ApolloClient
): ContestHistoryRepoInterface{
    override suspend fun loadNewContestData(name: Optional<String?>) {
        try {
            withContext(Dispatchers.IO) {
                val response = client.query(GetContestHistoryQuery(username = name)).execute()
                val history= response.data?.userContestRankingHistory
                if (response.data != null && history!=null) {
                    contestDao.deleteAllContest()
                    history.forEach {
                        contestDao.insertContest(contest = ContestEntity(
                            it?.contest?.title.toString(),
                            it?.contest?.startTime!!.toLong(),
                            it.contest.duration!!
                        ))
                    }
                } else {
                    e("error", "unable to fetch data")
                }
            }
        }catch (e: Exception){
            e("error",e.message.toString())
        }
    }

    override suspend fun loadContestDataIfEmptyDB() {
        if(contestDao.countRows()==0){
            loadNewContestData(Optional.presentIfNotNull("loft_plyr"))
        }
    }
}