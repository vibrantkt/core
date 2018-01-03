package org.vibrant.core.base

import org.vibrant.core.reducers.SignatureProducer
import org.vibrant.core.reducers.TransactionSummarizer
import org.vibrant.core.producers.TransactionProducer
import java.security.KeyPair


/***
 * [BaseTransactionModel] producer class
 *
 * @property from address of sender
 * @property to address of receiver
 * @property payload transaction payload
 * @property keyPair keyPair, which will be used to create signature
 * @property signatureProducer create signature
 * @property transactionSummarizer extract content from transaction
 *
 */
open class BaseTransactionImpl(
        val from: ByteArray,
        val to: ByteArray,
        val payload: ByteArray,
        private val keyPair: KeyPair,
        private val signatureProducer: SignatureProducer,
        private val transactionSummarizer: TransactionSummarizer
): TransactionProducer<BaseTransactionModel>() {
    override fun produce(): BaseTransactionModel {
        return BaseTransactionModel(
                from,
                to,
                payload,
                signatureProducer.produceSignature(transactionSummarizer.sum(this), keyPair)
        )
    }
}
