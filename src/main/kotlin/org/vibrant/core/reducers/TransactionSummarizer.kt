package org.vibrant.core.reducers

import org.vibrant.core.base.producers.BaseTransactionProducer

interface TransactionSummarizer{
    fun sum(transaction: BaseTransactionProducer): ByteArray
}