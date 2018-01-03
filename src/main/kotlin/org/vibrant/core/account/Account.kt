package org.vibrant.core.account

import com.fasterxml.jackson.annotation.JsonIgnore
import org.vibrant.core.util.HashUtils
import java.security.KeyPair

class Account(@JsonIgnore val keyPair: KeyPair){
//    val publicKey = HashUtils.bytesToHex(keyPair.public.encoded)
}