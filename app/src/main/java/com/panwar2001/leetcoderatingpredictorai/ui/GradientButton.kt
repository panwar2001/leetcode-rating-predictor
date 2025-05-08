package com.panwar2001.leetcoderatingpredictorai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GradientButton(
    modifier: Modifier= Modifier,
    gradient: Brush = Brush.horizontalGradient(colors = listOf(color1, color2)),
    onClick: () -> Unit,
    content:@Composable ()-> Unit
) {
    Button (
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        onClick = onClick,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = 14.dp, vertical = 2.dp),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}


val color1 = Color(0xFF642873)
val color2 = Color(0xFFC6426E)
//val color1 = Color(0xFF642873) // Deep Purple
//val color2 = Color(0xFFC6426E) // Vibrant Pink
//val color1 = Color(0xFF00BCD4) // Cyan
//val color2 = Color(0xFF009688) // Teal

//val color1= Color(0xFF42A5F5)
//val color2 = Color(0xFF478DE0)