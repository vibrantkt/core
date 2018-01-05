package org.vibrant.core.base.producers

import org.vibrant.core.base.models.BaseMessageModel
import org.vibrant.core.base.models.BaseTransactionModel
import org.vibrant.core.reducers.SignatureProducer
import org.vibrant.core.reducers.TransactionSummarizer
import org.vibrant.core.producers.TransactionProducer
import org.vibrant.core.reducers.ModelSerializer
import org.vibrant.core.util.HashUtils
import java.security.KeyPair


/***
 * [BaseTransactionModel] producer class
 *
 * @property from address of sender
 * @property to address of receiver
 * @property payload transaction payload
 * @property keyPair keyPair, which will be used to create signature
 * @property signatureProducer create signature
 *
 */
open class BaseTransactionProducer(
        private val from: String,
        private val to: String,
        private val payload: BaseMessageModel,
        private val keyPair: KeyPair,
        private val signatureProducer: SignatureProducer
): TransactionProducer<BaseTransactionModel>() {
    override fun produce(serializer: ModelSerializer): BaseTransactionModel {
        return BaseTransactionModel(
                from,
                to,
                payload,
                HashUtils.bytesToHex(
                        signatureProducer.produceSignature((this.from + this.to + serializer.serialize(this.payload)).toByteArray(), keyPair)
                )
        )
    }
}
