package com.panwar2001.leetcoderatingpredictorai.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panwar2001.leetcoderatingpredictorai.core.UserRepository
import com.panwar2001.leetcoderatingpredictorai.ui.getTime
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
    val ratingDelta: Float= 0f
)
/**
 * @PredictionViewModel fetch user data and predicts the rating of last contest, if rating is not updated
 * on the leetcode website.

 * @param userRepository handles user data access and processing
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

    /**
     * @predict fetch users all contest history & leetcode data then predicts rating on
     * the latest contest data.
     *
     * @param username
     * @param success
     */
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

    /**
     * @predictRatingDelta predicts the change in the rating and
     * update the prediction status
     */
    internal fun predictRatingDelta(){
        // if attempted no contest then no rating change
        if(_contestData.value.isEmpty()) {
            _predictionStatus.update { it.copy(ratingDelta = 0f) }
        }
         viewModelScope.launch {
             val latestContestAttended = _contestData.value[0]
             // if contest rating not updated on the dashboard then predict else return rating change
             val  delta = if(
                 contestMetaData.value.rating === latestContestAttended.rating
                 ){
              userRepository.predictUserRating(
                userRating = latestContestAttended.rating.toFloat(),
                contestAttended = _contestMetaData.value.attendedContestCount.toFloat(),
                userRank = latestContestAttended.ranking.toFloat(),
                contestTitle = latestContestAttended.title
            ) } else contestMetaData.value.rating.toFloat()-latestContestAttended.rating.toFloat()
             _predictionStatus.update { it.copy(ratingDelta = delta) }
        }
    }

    /**
     * @updateContestData Transform each historyItem into a ContestData instance,
     * update data in state
     *
     * @param userContestRankingHistory data about each contest in a list
     */
    internal fun updateContestData(
        userContestRankingHistory:  List<GetUserProfileQuery.UserContestRankingHistory>
    ){
        _contestData.update { currentList ->
            userContestRankingHistory.mapNotNull { historyItem ->
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

    /**
     * update user rating in a state to pass down to composable
     *
     * @param matchedUser user rating data
     */
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

    /**
     * @updateUserMetaData store user data in a state
     *
     * @param userContestRanking user data fetched from leetcode graphQL API
     */
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

    /**
     * @setUnableToPredict sets unable to predict status to  true ,if unable to predict
     * else reset to false
     *
     * @param value prediction status
     */
    fun setUnableToPredict(value: Boolean){
        _predictionStatus.update {
            it.copy(unableToPredict = value)
        }
    }
}

/**
 * @getTime convert unix time to string format
 *
 * @param unixTimeInSeconds time in seconds converted to milliseconds then format time.
 *
 * @return  time in string format
 * @sample
 * 2024-03-22 11:13:00
 */
fun getTime(unixTimeInSeconds: Long): String{
    val date = Date(unixTimeInSeconds * 1000)
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(date)
}
