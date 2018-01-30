package org.vibrant.base.database.tangle

import org.vibrant.base.database.blockchain.models.HashedTransaction
import org.vibrant.base.database.blockchain.models.TransactionPayload

open class TangleVertex<out T: TransactionPayload>(
        val parents: List<String>,
        override val hash: String,
        val payload: T): HashedTransaction(hash){
    override fun toString(): String {
        return "(parents=[${parents.joinToString(", ")}], hash=$hash, payload=$payload)"
    }
}