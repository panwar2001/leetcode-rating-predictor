package com.panwar2001.leetcoderatingpredictorai

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.apollographql.apollo.ApolloClient
import com.panwar2001.leetcoderatingpredictorai.core.UserRepositoryImpl
import com.panwar2001.leetcoderatingpredictorai.di.ContestRankingResponse
import com.panwar2001.leetcoderatingpredictorai.di.LeetCodeApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RatingPredictionTest {
    @Test
    fun testRatingPrediction()=runTest{
        val apolloClient = mockk<ApolloClient>()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val apiService = mockk<LeetCodeApiService>()
        coEvery { apiService.getContestRanking(any<String>())} returns ContestRankingResponse(9000)
        val userRepository = UserRepositoryImpl(
            apolloClient = apolloClient,
            context = context,
            apiService = apiService
        )
        assert(true)
        val delta= userRepository.predictUserRating(
            userRating = 1800f,
            contestAttended = 8f,
            userRank = 1800f,
            contestTitle = "Weekly contest 182"
        )
        println("Rating : $delta")
        assert(delta>0)
    }
}