package com.panwar2001.leetcoderatingpredictorai.ui

import com.panwar2001.leetcoderatingpredictorai.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AwaitingPrediction() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.awaiting_prediction_content),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                color = Color.Gray
            )
        }
    }
}
