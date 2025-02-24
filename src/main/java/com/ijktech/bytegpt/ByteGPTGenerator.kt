package com.ijktech.bytegpt

import android.content.Context
import ai.onnxruntime.*
import java.nio.LongBuffer

class ByteGPTGenerator(
    private val context: Context,
    private val modelPath: String = "model_mobile.ort",
    private val maxLength: Int = 512
) {
    private val env = OrtEnvironment.getEnvironment()
    private val session: OrtSession
    private val tokenizer = ByteGPTTokenizer()

    init {
        context.assets.open(modelPath).use { modelInput ->
            val modelBytes = modelInput.readBytes()
            session = env.createSession(modelBytes)
        }
    }

    fun generate(prompt: String, maxNewTokens: Int = 50, temperature: Float = 1.0f): String {
        var currentIds = tokenizer.encode(prompt)

        for (i in 0 until maxNewTokens) {
            if (currentIds.size >= maxLength) break

            val shape = longArrayOf(1, currentIds.size.toLong())
            val tensorInput = OnnxTensor.createTensor(env, LongBuffer.wrap(currentIds), shape)

            val output = session.run(mapOf("input" to tensorInput), setOf("output"))
            // Fix: Convert float[][][] to Float[][][]
            @Suppress("UNCHECKED_CAST")
            val rawLogits = output[0].value as Array<Any>
            val logits = rawLogits.map { it as Array<FloatArray> }.toTypedArray()

            // Get logits for the last token
            val lastTokenLogits = logits[0].last()
            if (temperature != 1.0f) {
                for (j in lastTokenLogits.indices) {
                    lastTokenLogits[j] /= temperature
                }
            }

            val expLogits = lastTokenLogits.map { Math.exp(it.toDouble()) }
            val sum = expLogits.sum()
            val probs = expLogits.map { it / sum }

            val random = Math.random()
            var cumsum = 0.0
            var nextToken = 0
            for (j in probs.indices) {
                cumsum += probs[j]
                if (random < cumsum) {
                    nextToken = j
                    break
                }
            }

            currentIds = currentIds.plus(nextToken.toLong())
            if (nextToken.toLong() == ByteGPTTokenizer.EOS_ID) break
        }

        return tokenizer.decode(currentIds)
    }
}
