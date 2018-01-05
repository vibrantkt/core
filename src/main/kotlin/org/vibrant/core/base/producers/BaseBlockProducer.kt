package org.vibrant.core.base.producers

import org.vibrant.core.base.BaseJSONSerializer
import org.vibrant.core.base.models.BaseBlockModel
import org.vibrant.core.base.models.BaseTransactionModel
import org.vibrant.core.models.TransactionModel
import org.vibrant.core.producers.BlockProducer
import org.vibrant.core.reducers.HashProducer
import org.vibrant.core.reducers.ModelSerializer
import org.vibrant.core.util.HashUtils
import java.util.*


class BaseBlockProducer(
        private val index: Long,
        private val prevBlockHash: String,
        private val timestamp: Long = Date().time,
        private val transactions: List<BaseTransactionModel>
): BlockProducer<BaseBlockModel>(){



    override fun produce(serializer: ModelSerializer): BaseBlockModel {
        val transactionsContent = transactions.map {
            return@map serializer.serialize(it)
        }.joinToString("")
        val payload = this.index.toString() + this.prevBlockHash + this.timestamp + transactionsContent
        val hash = HashUtils.bytesToHex(HashUtils.sha256(payload.toByteArray()))
        return BaseBlockModel(
                index,
                hash,
                prevBlockHash,
                timestamp,
                transactions.toList()
        )
    }
}