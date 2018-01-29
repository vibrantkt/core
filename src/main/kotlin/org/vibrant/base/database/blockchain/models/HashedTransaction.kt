package org.vibrant.base.database.blockchain.models

abstract class HashedTransaction(open val hash: String): TransactionModel()