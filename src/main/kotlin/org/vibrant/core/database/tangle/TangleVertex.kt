package org.vibrant.core.database.tangle

import org.vibrant.core.models.transaction.HashedTransactionModel
import org.vibrant.core.models.transaction.TransactionPayload

open class TangleVertex<out T: TransactionPayload>(
        val parents: List<String>,
        override val hash: String,
        val payload: T): HashedTransactionModel(hash){

    override fun toString(): String {
        return "TangleVertex(parents=$parents, hash='$hash', payload=$payload)"
    }
}