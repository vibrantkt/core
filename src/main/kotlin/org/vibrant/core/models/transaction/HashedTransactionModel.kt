package org.vibrant.core.models.transaction

abstract class HashedTransactionModel(open val hash: String): TransactionModel()