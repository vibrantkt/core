package org.vibrant.core.reducers

import java.security.KeyPair

/***
 * Creates signature of given payload and keypair
 */
interface SignatureProducer {
    /***
     * @param content payload to sign
     * @param keyPair key pair to use
     * @return signature
     */
    fun produceSignature(content: ByteArray, keyPair: KeyPair): ByteArray
}