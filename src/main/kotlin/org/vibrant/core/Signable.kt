package org.vibrant.core

import org.vibrant.core.util.AccountUtils
import java.security.KeyPair

interface Signable{
    var signature: String?

    fun sign(keyPair: KeyPair, data: String): ByteArray {
        return AccountUtils.signData(data, keyPair)
    }
}