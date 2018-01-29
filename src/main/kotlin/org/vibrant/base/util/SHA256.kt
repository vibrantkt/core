package org.vibrant.base.util

import org.vibrant.core.algorithm.HashProducer

class SHA256: HashProducer {
    override fun produceHash(data: ByteArray): ByteArray {
        return HashUtils.sha256(data)
    }
}