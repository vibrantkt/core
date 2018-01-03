package org.vibrant.core

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import org.vibrant.core.reducers.HashProducer
import org.vibrant.core.reducers.SignatureProducer
import org.vibrant.core.reducers.TransactionSummarizer
import org.vibrant.core.base.BaseTransactionImpl
import org.vibrant.core.util.HashUtils
import java.security.KeyPair
import java.security.KeyPairGenerator

class TestTransactionBuilder {


    @Test
    fun `Test hash producer`(){
        val o = object: HashProducer {
            override fun produceHash(data: ByteArray): ByteArray {
                return HashUtils.sha256(data)
            }
        }
        assertArrayEquals(
                HashUtils.sha256(byteArrayOf(1, 2, 3)),
                o.produceHash(byteArrayOf(1, 2, 3))
        )
    }

    @Test
    fun `Test signature producer`(){
        val o = object: SignatureProducer {
            override fun produceSignature(content: ByteArray, keyPair: KeyPair): ByteArray {
                return HashUtils.signData(content, keyPair)
            }
        }

        val kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(1024)
        val keyPair = kpg.genKeyPair()

        assertArrayEquals(
                HashUtils.signData(byteArrayOf(1, 2, 3), keyPair),
                o.produceSignature(byteArrayOf(1, 2, 3), keyPair)
        )
    }

    @Test
    fun `Test transaction producer`(){
        val signatureProducer = object: SignatureProducer {
            override fun produceSignature(content: ByteArray, keyPair: KeyPair): ByteArray {
                return HashUtils.signData(content, keyPair)
            }
        }

        val transactionSummarizer = object: TransactionSummarizer {
            override fun sum(transaction: BaseTransactionImpl): ByteArray {
                return transaction.from + transaction.to + transaction.payload
            }

        }

        val kpg = KeyPairGenerator.getInstance("RSA")
        kpg.initialize(1024)
        val senderPair = kpg.genKeyPair()
        val receiverPair = kpg.genKeyPair()

        val transactionBuilder = BaseTransactionImpl(
                senderPair.public.encoded,
                receiverPair.public.encoded,
                byteArrayOf(1, 2, 3, 4, 5, 6),
                senderPair,
                signatureProducer,
                transactionSummarizer
        )
        val transaction = transactionBuilder.produce()
        // addresses are fine
        assertArrayEquals(
                senderPair.public.encoded,
                transaction.from
        )
        // addresses are fine
        assertArrayEquals(
                receiverPair.public.encoded,
                transaction.to
        )
        // payload stays still
        assertArrayEquals(
                byteArrayOf(1, 2, 3, 4, 5, 6),
                transaction.payload
        )
        // signature ok
        assertArrayEquals(
                signatureProducer.produceSignature(transactionSummarizer.sum(transactionBuilder), senderPair),
                transaction.signature
        )
    }
}