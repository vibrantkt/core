package org.vibrant.core.hash

import org.vibrant.core.hash.HashProducer
import java.security.MessageDigest

object SHA1 : HashProducer {
    override fun produceHash(data: ByteArray): ByteArray {
        return MessageDigest
                .getInstance("SHA-1")
                .digest(data)
    }
}