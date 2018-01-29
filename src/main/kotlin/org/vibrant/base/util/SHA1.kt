package org.vibrant.base.util

import org.vibrant.core.algorithm.HashProducer

class SHA1 : HashProducer {
    override fun produceHash(data: ByteArray): ByteArray {
        return HashUtils.sha1(data)
    }
}