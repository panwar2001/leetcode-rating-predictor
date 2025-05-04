package com.panwar2001.leetcoderatingpredictorai.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun NetworkOffScreen(isOffline:Boolean,modifier: Modifier= Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier=modifier.fillMaxSize()
    ){
        if(isOffline) {
            Text(
                text = "Oops! No Internet",
                color = Color.Gray,
                fontWeight = FontWeight.Bold
            )
        }else{
            Row {
                CircularProgressIndicator()
                Text(" Loading...")
            }
        }
    }
}