package com.panwar2001.leetcoderatingpredictorai.viewModels


import android.R.attr.data
import android.R.attr.rating
import android.util.Half.toFloat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.leetcoderatingpredictorai.core.UserRepository
import com.panwar2001.prediction.GetUserProfileQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import kotlin.collections.mapNotNull
import kotlin.math.round

data class ContestData(
    val title: String="",
    val startTime: String="",
    val rating: String="",
    val ranking: String="",
    val problemsSolved: String=""
)
data class ProblemsSolved(
    val easy: Int = 0 ,
    val medium: Int =0,
    val hard: Int = 0,
    val total: Int = 0
)
data class ContestMetaData(
    val attendedContestCount: String = "",
    val rating: String = "",
    val globalRanking:  String= "",
    val topPercentage: String =""
)

data class PredictionStatus(
    val loading: Boolean = false,
    val unableToPredict: Boolean = false,
    val predictedRating: String
)
/**
 * @PredictionViewModel fetch user data and predicts the rating of last contest, if rating is not updated
 * on the leetcode website.
 *
 */
@HiltViewModel
class PredictionViewModel @Inject
constructor(private val userRepository: UserRepository): ViewModel(){
    private val _contestData = MutableStateFlow(listOf<ContestData>())
    private val _problemsSolved = MutableStateFlow(ProblemsSolved())
    private val _contestMetaData = MutableStateFlow(ContestMetaData())
    private val _predictionStatus = MutableStateFlow(PredictionStatus())

    val contestData: StateFlow<List<ContestData>> = _contestData.asStateFlow()
    val problemsSolved: StateFlow<ProblemsSolved> = _problemsSolved.asStateFlow()
    val contestMetaData: StateFlow<ContestMetaData> = _contestMetaData.asStateFlow()
    val predictionStatus: StateFlow<PredictionStatus> = _predictionStatus.asStateFlow()

    fun predict(
        username: String,
        success: (Boolean) ->Unit
    ) {
        _predictionStatus.update { it.copy(loading = true, unableToPredict = false) }
        viewModelScope.launch {
            val result = userRepository.getUserProfile(username)
            result.onSuccess { data ->
                updateContestData(data.userContestRankingHistory)
                updateProblemSolved(data.matchedUser)
                updateUserMetaData(data.userContestRanking)
                predictRatingDelta()
                success(true)
                _predictionStatus.update { it.copy(loading = false, unableToPredict = false) }
            }.onFailure {
                success(false)
                _predictionStatus.update { it.copy(loading = false, unableToPredict = true) }
            }
        }
    }
    internal fun predictRatingDelta(){
        if(_contestData.value.isEmpty()) {
            _predictionStatus.update { it.copy(predictedRating = "0") }
        }
        val latestContestAttended = _contestData.value[0]
         viewModelScope.launch {
            val delta = userRepository.predictUserRating(
                userRating = latestContestAttended.rating.toFloat(),
                contestAttended = _contestMetaData.value.attendedContestCount.toFloat(),
                userRank = latestContestAttended.ranking.toFloat(),
                contestTitle = latestContestAttended.title
            )
             val predictedRating = latestContestAttended.rating.toFloat() + delta
             _predictionStatus.update { it.copy(predictedRating = predictedRating.toString()) }
        }
    }
    internal fun updateContestData(
        userContestRankingHistory:  List<GetUserProfileQuery.UserContestRankingHistory>
    ){
        _contestData.update { currentList ->
            userContestRankingHistory.mapNotNull { historyItem ->
                // Transform each historyItem into a ContestData instance
                if(historyItem.attended) {
                    ContestData(
                        title = historyItem.contest.title,
                        startTime = getTime(historyItem.contest.startTime.toLong()),
                        rating = historyItem.rating.toString(),
                        ranking = historyItem.ranking.toString(),
                        problemsSolved = historyItem.problemsSolved.toString(),
                    )
                }else{
                    null
                }
            }.sortedByDescending { it.startTime }
        }
    }
    internal fun updateProblemSolved(matchedUser: GetUserProfileQuery.MatchedUser){
        _problemsSolved.update {
            var easy = 0
            var medium = 0
            var hard = 0
            var total = 0
            matchedUser.submitStats.acSubmissionNum.forEach {
                if(it.difficulty =="Easy")
                    easy= it.count
                else if(it.difficulty=="Medium")
                    medium = it.count
                else if(it.difficulty=="Hard")
                    hard = it.count
                else
                    total = it.count
            }
            it.copy(
                easy = easy,
                medium = medium,
                hard = hard,
                total = total
            )
        }
    }
   internal fun updateUserMetaData(
        userContestRanking:  GetUserProfileQuery.UserContestRanking
    ){
        userContestRanking.let{ it->
            _contestMetaData.update { metaData->
                metaData.copy(
                    attendedContestCount = "${it.attendedContestsCount}",
                    rating= "${round(it.rating)}",
                    globalRanking = "${it.globalRanking}",
                    topPercentage = "${it.topPercentage}"
                )
            }
        }
    }
    fun setUnableToPredict(value: Boolean){
        _predictionStatus.update {
            it.copy(unableToPredict = value)
        }
    }
}

fun getTime(unixTimeInSeconds: Long): String{
    val date = Date(unixTimeInSeconds * 1000)
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(date)
}
