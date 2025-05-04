package com.panwar2001.leetcoderatingpredictorai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.panwar2001.leetcoderatingpredictorai.ui.NavigationController
import com.panwar2001.leetcoderatingpredictorai.ui.theme.LeetcodeRatingPredictorAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LeetcodeRatingPredictorAITheme {
                NavigationController()
            }
        }
    }
}

