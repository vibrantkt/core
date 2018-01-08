package org.vibrant.extend

import org.junit.Assert.assertEquals
import org.junit.Test
import org.vibrant.core.base.producers.BaseTransactionProducer
import org.vibrant.core.reducers.SignatureProducer
import org.vibrant.core.base.BaseJSONSerializer
import org.vibrant.core.base.models.BaseMessageModel
import org.vibrant.core.base.models.BaseTransactionModel
import org.vibrant.core.util.AccountUtils
import org.vibrant.core.util.HashUtils
import java.security.KeyPair
import java.util.*

class TestBaseTransaction {


    @Test
    fun `Base transaction producer`() {
        val sender = AccountUtils.generateKeyPair()
        val transaction = BaseTransactionProducer(
                "yura",
                "vasya",
                BaseMessageModel("Hello!", 0),
                sender,
                object : SignatureProducer{
                    override fun produceSignature(content: ByteArray, keyPair: KeyPair): ByteArray {
                        return HashUtils.signData(content, keyPair)
                    }
                }
        ).produce(BaseJSONSerializer())

        assertEquals(
                "yura",
                transaction.from
        )

        assertEquals(
                "vasya",
                transaction.to
        )

        assertEquals(
                BaseMessageModel("Hello!",0),
                transaction.payload
        )

        assertEquals(
                HashUtils.bytesToHex(AccountUtils.signData("yuravasya" + BaseJSONSerializer().serialize(BaseMessageModel("Hello!",0)), sender)),
                transaction.signature
        )

    }

    @Test
    fun `Base transaction deserialization`() {
        val sender = AccountUtils.generateKeyPair()
        val transaction = BaseTransactionProducer(
                "yura",
                "vasya",
                BaseMessageModel("Hello!", 0),
                sender,
                object : SignatureProducer{
                    override fun produceSignature(content: ByteArray, keyPair: KeyPair): ByteArray {
                        return HashUtils.signData(content, keyPair)
                    }
                }
        ).produce(BaseJSONSerializer())

        val converted = BaseJSONSerializer().deserialize(BaseJSONSerializer().serialize(transaction)) as BaseTransactionModel

        assertEquals(
                "yura",
                converted.from
        )

        assertEquals(
                "vasya",
                converted.to
        )

        assertEquals(
                BaseMessageModel("Hello!", 0),
                converted.payload
        )

        assertEquals(
                HashUtils.bytesToHex(AccountUtils.signData("yuravasya" + BaseJSONSerializer().serialize(BaseMessageModel("Hello!",0)), sender)),
                converted.signature
        )
    }

    @Test
    fun `Base transaction serialization`() {
        val sender = AccountUtils.generateKeyPair()
        val transaction = BaseTransactionProducer(
                "yura",
                "vasya",
                BaseMessageModel("Hello!", 0),
                sender,
                object : SignatureProducer{
                    override fun produceSignature(content: ByteArray, keyPair: KeyPair): ByteArray {
                        return HashUtils.signData(content, keyPair)
                    }
                }
        ).produce(BaseJSONSerializer())

        val serialized = BaseJSONSerializer().serialize(transaction)
        assertEquals(
                "{\"@type\":\"transaction\",\"from\":\"yura\",\"to\":\"vasya\",\"payload\":{\"@type\":\"message\",\"content\":\"Hello!\",\"timestamp\":0},\"signature\":\"${transaction.signature}\"}",
                serialized
        )
    }
}