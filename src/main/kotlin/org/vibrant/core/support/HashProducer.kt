package org.vibrant.core.support

interface HashProducer {
    fun produceHash(data: ByteArray): ByteArray
}