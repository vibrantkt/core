package org.vibrant.core.reducers

interface HashProducer {
    fun produceHash(data: ByteArray): ByteArray
}