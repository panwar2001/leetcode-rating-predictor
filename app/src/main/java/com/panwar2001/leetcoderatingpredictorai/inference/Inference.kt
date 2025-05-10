package com.panwar2001.leetcoderatingpredictorai.inference

import android.content.Context
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.FloatBuffer

data class UserInput(
    val userRating: Float,
    val contestAttended: Float,
    val userRank: Float,
    val totalParticipants: Float
)
fun getUserInputArray(input: UserInput): FloatArray {
    val relativeRank = if (input.totalParticipants != 0f)
        (input.userRank * 100f) / input.totalParticipants
    else 0f

    return floatArrayOf(
        input.userRating,
        input.userRank,
        input.totalParticipants,
        relativeRank,
        input.contestAttended
    )
}

fun scaleInputData(
    inputData: FloatArray,
    minValues: FloatArray,
    maxValues: FloatArray
): Array<Array<FloatArray>> {
    val scaled = inputData.mapIndexed { i, value ->
        (value - minValues[i]) / (maxValues[i] - minValues[i])
    }.toFloatArray()

    return arrayOf(arrayOf(scaled))
}

fun runModel(context: Context, inputData: Array<Array<FloatArray>>): Float {
    val litertBuffer =FileUtil.loadMappedFile(context, "model.tflite")
    val interpreter = Interpreter(litertBuffer, Interpreter.Options())
    val inputShape = interpreter.getInputTensor(0).shape()
    val outputShape = interpreter.getOutputTensor(0).shape()
    val inputBuffer = FloatBuffer.allocate(inputShape.reduce { acc,i -> acc*i })
    inputData[0][0].forEach { inputBuffer.put(it) }
    inputBuffer.rewind()
    val outputBuffer = FloatBuffer.allocate(outputShape.reduce { acc,i -> acc*i })
    interpreter.run(inputBuffer, outputBuffer)
    outputBuffer.rewind()
    val output = FloatArray(outputBuffer.capacity())
    outputBuffer.get(output)
    return output[0]
}

