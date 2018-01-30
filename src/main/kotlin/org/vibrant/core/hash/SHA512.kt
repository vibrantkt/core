package org.vibrant.core.hash

import java.security.MessageDigest

object SHA512 : HashProducer {
    override fun produceHash(data: ByteArray): ByteArray {
        return MessageDigest
                .getInstance("SHA-512")
                .digest(data)
    }
}