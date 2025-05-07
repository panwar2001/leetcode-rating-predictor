package com.panwar2001.leetcoderatingpredictorai.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.leetcoderatingpredictorai.ui.theme.LeetcodeRatingPredictorAITheme
import com.panwar2001.leetcoderatingpredictorai.viewModels.ContestData
import com.panwar2001.leetcoderatingpredictorai.viewModels.ContestMetaData
import com.panwar2001.leetcoderatingpredictorai.viewModels.ProblemsSolved

@Composable
fun RatingOverviewScreen(
    contest: List<ContestData> ,
    problemsSolved: ProblemsSolved ,
    contestMetaData: ContestMetaData,
    predictedRating: Float
) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Your Rating Overview",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            RatingSection(title = "Current Rating", rating = contestMetaData.rating)
            Spacer(modifier = Modifier.height(16.dp))
            RatingSection(title = "Predicted Rating", rating = "$predictedRating (↑ ${200})")

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            Text(
                text = "Problem-Solving Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ChartCirclePie(
                    charts = charts,
                    problemsSolved = problemsSolved.total.toString()
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("\uD83D\uDFE9 Easy: ${problemsSolved.easy}",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.W600)
                    Text("\uD83D\uDFE7 Medium: ${problemsSolved.medium}",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.W600)
                    Text("\uD83D\uDFE5 Hard: ${problemsSolved.hard}",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.W600)
                }
            }

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            Text(
                text = "Contest History (Attended ${contest.size} contest)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(contest){
                    ContestHistoryItem(
                        title = it.title,
                        date = it.startTime,
                        rating = it.rating,
                        rank = it.ranking
                    )
                }
            }
        }
}

@Composable
fun RatingSection(title: String, rating: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = rating,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun ContestHistoryItem(title: String, date: String, rating: String, rank: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Date: $date, Rating: $rating, Rank: $rank",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    val contest = remember { listOf<ContestData>(
        ContestData(
        title = "LeetCode Weekly Contest 200",
        startTime = "18 march"  ,
        rating = "1800",
        ranking = "4000",
        problemsSolved = "3",
       ),
        ContestData(
            title = "LeetCode Weekly Contest 201",
            startTime = "340303",
            rating = "1800",
            ranking = "4000",
            problemsSolved = "3",
        ),
        ContestData(
            title = "LeetCode Weekly Contest 201",
            startTime = "340303",
            rating = "1800",
            ranking = "4000",
            problemsSolved = "3",
        )
        )}
    val contestMetaData = remember {  ContestMetaData(
        attendedContestCount = "20",
        rating= "445",
        globalRanking= "5435",
        topPercentage= "454"
    )}
    val problemsSolved = remember {  ProblemsSolved(
        easy = "234",
        medium = "43534",
        hard = "434",
        total = "4534"
    )}

    LeetcodeRatingPredictorAITheme {
        RatingOverviewScreen(
            contest= contest,
            problemsSolved = problemsSolved ,
            contestMetaData= contestMetaData,
            predictedRating = 2000f
        )
    }
}
