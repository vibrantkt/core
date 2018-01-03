package org.vibrant.core.reducers

import org.vibrant.core.base.BaseTransactionImpl

interface TransactionSummarizer{
    fun sum(transaction: BaseTransactionImpl): ByteArray
}