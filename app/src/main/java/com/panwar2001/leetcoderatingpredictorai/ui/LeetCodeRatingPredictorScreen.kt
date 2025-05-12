package com.panwar2001.leetcoderatingpredictorai.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panwar2001.leetcoderatingpredictorai.R
import com.panwar2001.leetcoderatingpredictorai.database.ContestEntity
import com.panwar2001.leetcoderatingpredictorai.ui.theme.LeetcodeRatingPredictorAITheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun LeetCodeRatingPredictorScreen(
    contests: List<ContestEntity>,
    isRefreshing: Boolean = false,
    reloadContest: ()->Unit= {},
    predict: (String)->Unit,
    isPredicting: Boolean = false,
    unableToPredict: Boolean = false,
    maxLengthInput: Int = 50,
    usernamePattern: Regex = Regex("[a-zA-Z0-9_]*$"),
    setUnableToPredict: (Boolean)-> Unit,
    isNetworkAvailable: Boolean= false
) {
        var username by remember { mutableStateOf("") }
        val focusManager = LocalFocusManager.current

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .clickable {
                    focusManager.clearFocus() // Hide keyboard on click outside
                }
        ) {
            Text(
                text = stringResource(R.string.title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter Your LeetCode Username",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {
                    if (it.length <= maxLengthInput && it.matches(usernamePattern)) {
                        username = it
                        if(unableToPredict){
                            setUnableToPredict(false)
                        }
                    }
                },
                leadingIcon = {
                    Text(
                        if(isNetworkAvailable) "\uD83D\uDFE2"
                        else "\uD83D\uDD34"
                    )
                },
                placeholder = { Text("Username") },
                singleLine = true,
                trailingIcon = {
                    Icon(Icons.Default.Clear,"",
                        modifier = Modifier.clickable(onClick={
                            if(username!="")
                               username=""
                            else
                              focusManager.clearFocus()
                        }))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (unableToPredict) {
                            Modifier.border(
                                width = 2.dp,
                                color = Color.Red,
                                shape = RoundedCornerShape(12.dp)
                            )
                        } else {
                            Modifier
                        }
                    ),
                shape = RoundedCornerShape(12.dp),
                isError = unableToPredict,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    },
                    onGo = {
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { focusManager.clearFocus(); predict(username) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if(isPredicting){
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.then(Modifier.size(20.dp)),
                        strokeWidth = 2.dp
                    )
                }else {
                    Text(
                        text = stringResource(R.string.analyze),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically){
                Text(text = "\uD83C\uDFC6 LeetCode Contests (${contests.size})",
                    fontWeight = FontWeight.Bold)
                GradientButton(onClick = reloadContest){
                    Row {
                        if (isRefreshing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.then(Modifier.size(16.dp)),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reload"
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Reload",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(contests){
                    ContestItem(contest= it)
                }
            }
        }
}


@Composable
fun ContestItem(contest: ContestEntity) {
    var expanded by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clickable { expanded = !expanded }
            .animateContentSize(),
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = contest.contestName,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text("Start Time: ${getTime(contest.time)}")
                    Text("Duration: ${getDuration(contest.duration)}")
                }
            }
        }
    }
}
fun getTime(unixTimeInSeconds: Long): String{
    val date = Date(unixTimeInSeconds * 1000)
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(date)
}
fun getDuration(durationInSeconds: Int): String {
    val hours = durationInSeconds / 3600
    val minutes = (durationInSeconds % 3600) / 60

    return buildString {
        if (hours > 0) append("$hours hour${if (hours > 1) "s" else ""} ")
        if (minutes > 0) append("$minutes minute${if (minutes > 1) "s" else ""}")
        if (hours == 0 && minutes == 0) append("less than a minute")
    }.trim()
}


@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    val contests = mutableListOf<ContestEntity>()
    repeat(5){
        contests.add(ContestEntity("Weekly contest ${6-it+1}", 939349949, 1233))
    }
    LeetcodeRatingPredictorAITheme {
        LeetCodeRatingPredictorScreen(
            contests = contests,
            predict = {},
            setUnableToPredict = {}
        )
    }
}
