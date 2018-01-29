package org.vibrant.base.util

import org.vibrant.core.algorithm.HashProducer

object SHA512 : HashProducer {
    override fun produceHash(data: ByteArray): ByteArray {
        return HashUtils.sha512(data)
    }
}