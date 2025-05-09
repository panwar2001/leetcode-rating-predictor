package com.panwar2001.leetcoderatingpredictorai.core

import android.content.Context
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import org.tensorflow.lite.Interpreter
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
fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
    val fileDescriptor = context.assets.openFd(modelPath)
    val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
    val fileChannel = inputStream.channel
    return fileChannel.map(
        FileChannel.MapMode.READ_ONLY,
        fileDescriptor.startOffset,
        fileDescriptor.declaredLength
    )
}
fun runModel(context: Context, inputData: Array<Array<FloatArray>>): Float {
    val interpreter = Interpreter(loadModelFile(context, "model.tflite"))

    val inputBuffer = ByteBuffer.allocateDirect(4 * 1 * 1 * 5).order(ByteOrder.nativeOrder())
    inputData[0][0].forEach { inputBuffer.putFloat(it) }
    inputBuffer.rewind()

    val outputBuffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder())
    interpreter.run(inputBuffer, outputBuffer)

    outputBuffer.rewind()
    return outputBuffer.float
}