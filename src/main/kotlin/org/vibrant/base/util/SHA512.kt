package org.vibrant.base.util

import org.vibrant.core.algorithm.HashProducer

class SHA512 : HashProducer {
    override fun produceHash(data: ByteArray): ByteArray {
        return HashUtils.sha512(data)
    }
}