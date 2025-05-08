package com.panwar2001.leetcoderatingpredictorai.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.panwar2001.leetcoderatingpredictorai.ui.theme.Orange

data class ChartModel(
    val value: Float,
    val color: Color,
)
fun getCharts(easy:Int, medium:Int, hard: Int): List<ChartModel> {
    val total = (easy+medium+hard)*1f
    val easyAC = easy/total*100
    val mediumAC = medium/total*100
    val hardAC  = hard/total*100
    return listOf(
        ChartModel(value = easyAC, color = Color.Green),
        ChartModel(value = mediumAC, color = Orange),
        ChartModel(value = hardAC, color = Color.Red),
    )
}
@OptIn(ExperimentalTextApi::class)
@Composable
internal fun ChartCirclePie(
    modifier: Modifier = Modifier,
    charts: List<ChartModel>,
    size: Dp = 100.dp,
    strokeWidth: Dp = 7.dp,
    problemsSolved: String
) {
    val textMeasurer = rememberTextMeasurer()
    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        ) {
            append(problemsSolved)
        }
    }

    val textLayoutResult = textMeasurer.measure(annotatedText)
    val textSize = textLayoutResult.size.toSize()

    Canvas(
        modifier = modifier
            .size(size)
            .padding(12.dp)
    ) {
        var startAngle = 270f

        charts.forEach {
            val sweepAngle = (it.value / 100f) * 360f

            drawArc(
                color = it.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            startAngle += sweepAngle
        }

        drawText(
            textMeasurer = textMeasurer,
            text = annotatedText,
            topLeft = Offset(
                x = (this.size.width - textSize.width) / 2f,
                y = (this.size.height - textSize.height) / 2f
            )
        )
    }
}
