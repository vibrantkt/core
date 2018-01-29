package org.vibrant.base.util

import org.vibrant.core.algorithm.HashProducer

object SHA256: HashProducer {
    override fun produceHash(data: ByteArray): ByteArray {
        return HashUtils.sha256(data)
    }
}