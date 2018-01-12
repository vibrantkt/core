package org.vibrant.core.algorithm

interface HashProducer {
    fun produceHash(data: ByteArray): ByteArray
}