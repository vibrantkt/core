package org.vibrant.base.util

import org.vibrant.core.algorithm.HashProducer
import java.security.MessageDigest

object SHA1 : HashProducer {
    override fun produceHash(data: ByteArray): ByteArray {
        return MessageDigest
                .getInstance("SHA-1")
                .digest(data)
    }
}