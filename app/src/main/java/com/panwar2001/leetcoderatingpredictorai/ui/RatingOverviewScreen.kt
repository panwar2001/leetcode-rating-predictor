package com.panwar2001.leetcoderatingpredictorai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.panwar2001.leetcoderatingpredictorai.ui.theme.LeetcodeRatingPredictorAITheme

@Composable
fun RatingOverviewScreen() {
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

            RatingSection(title = "Current Rating", rating = "1600")
            Spacer(modifier = Modifier.height(16.dp))
            RatingSection(title = "Predicted Rating", rating = "1650 (↑ 50)")

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
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color.LightGray, CircleShape), // Placeholder for chart
                    contentAlignment = Alignment.Center
                ) {
                    Text("Pie Chart")
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("• Easy: 360")
                    Text("• Medium: 240")
                    Text("• Hard: 300")
                }
            }

            Divider(modifier = Modifier.padding(vertical = 24.dp))

            Text(
                text = "Contest History",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContestHistoryItem(
                title = "LeetCode Weekly Contest 200",
                date = "Sep 25, 2023",
                rating = "1500",
                rank = "50"
            )
            ContestHistoryItem(
                title = "LeetCode Biweekly Contest 81",
                date = "Sep 12, 2023",
                rating = "1650",
                rank = "20"
            )
            ContestHistoryItem(
                title = "LeetCode Weekly Contest 199",
                date = "Sep 01, 2023",
                rating = "1800",
                rank = "5"
            )
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
    LeetcodeRatingPredictorAITheme {
        RatingOverviewScreen()
    }
}
