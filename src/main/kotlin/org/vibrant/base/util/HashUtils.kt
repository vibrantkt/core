package org.vibrant.base.util

import java.security.MessageDigest


object HashUtils {

    private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

    private fun hashBytes(type: String, input: ByteArray): ByteArray {
        return MessageDigest
                .getInstance(type)
                .digest(input)
    }

    fun sha512(input: ByteArray) = hashBytes("SHA-512", input)

    fun sha256(input: ByteArray) = hashBytes("SHA-256", input)

    fun sha1(input: ByteArray) = hashBytes("SHA-1", input)


    fun bytesToHex(bytes: ByteArray): String {
        val result = StringBuffer()
        bytes.forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            result.append(HEX_CHARS[firstIndex])
            result.append(HEX_CHARS[secondIndex])
        }

        return result.toString()
    }

    fun hexToBytes(hex: String): ByteArray {
        val result = ByteArray(hex.length / 2)

        for (i in 0 until hex.length step 2) {
            val firstIndex = HEX_CHARS.indexOf(hex[i])
            val secondIndex = HEX_CHARS.indexOf(hex[i + 1])

            val octet = firstIndex.shl(4).or(secondIndex)
            result[i.shr(1)] = octet.toByte()
        }

        return result
    }
}