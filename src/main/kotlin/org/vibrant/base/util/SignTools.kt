package org.vibrant.base.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.Signature

object SignTools {

    fun signDataWith(data: ByteArray, algorithm: String, keyPair: KeyPair): ByteArray {
        val sig = Signature.getInstance(algorithm)
        sig.initSign(keyPair.private)
        sig.update(data)
        val signatureBytes = sig.sign()
        sig.initVerify(keyPair.public)
        sig.update(data)
        sig.verify(signatureBytes)
        return signatureBytes
    }


    fun verifyDataSignature(data: ByteArray, publicKey: PublicKey, signature: ByteArray, algorithm: String): Boolean {
        val sig = Signature.getInstance(algorithm)
        sig.initVerify(publicKey)
        sig.update(data)
        return sig.verify(signature)
    }


    fun serializeKeyPair(keyPair: KeyPair): ByteArray {
        val b = ByteArrayOutputStream()
        val o = ObjectOutputStream(b)
        o.writeObject(keyPair)
        return b.toByteArray()
    }


    fun deserializeKeyPair(byteArray: ByteArray): KeyPair{
        val bi = ByteArrayInputStream(byteArray)
        val oi = ObjectInputStream(bi)
        return oi.readObject() as KeyPair
    }



    fun generateKeyPair(algorithm: String): KeyPair {
        val kpg = KeyPairGenerator.getInstance(algorithm)
        kpg.initialize(1024)
        return kpg.genKeyPair()
    }
}