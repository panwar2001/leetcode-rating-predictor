package com.panwar2001.leetcoderatingpredictorai.core

import android.util.Log.e
import com.apollographql.apollo.ApolloClient
import com.panwar2001.leetcoderatingpredictorai.GetContestHistoryQuery
import com.panwar2001.leetcoderatingpredictorai.database.ContestDao
import com.panwar2001.leetcoderatingpredictorai.database.ContestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ContestHistoryRepoInterface {
    suspend fun loadNewContestData( name: String)
    suspend fun loadContestDataIfEmptyDB()
}

/**
 * @ContestHistoryRepository fetch list of past weekly & bi-weekly contest
 */
class ContestHistoryRepository  @Inject
constructor(
    private val contestDao: ContestDao,
    private val client: ApolloClient
): ContestHistoryRepoInterface{
    override suspend fun loadNewContestData(name: String) {
        try {
            withContext(Dispatchers.IO) {
                val response = client.query(GetContestHistoryQuery(username = name)).execute()
                val history= response.data?.userContestRankingHistory
                if (response.data != null && history!=null) {
                    contestDao.deleteAllContest()
                    history.forEach {
                        contestDao.insertContest(contest = ContestEntity(
                            it.contest.title,
                            it.contest.startTime.toLong(),
                            it.contest.duration
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
            loadNewContestData("loft_plyr")
        }
    }
}