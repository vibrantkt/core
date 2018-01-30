package org.vibrant.core.hash

interface HashProducer {
    fun produceHash(data: ByteArray): ByteArray
}