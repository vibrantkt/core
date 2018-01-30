package org.vibrant.core.database.tangle

import org.vibrant.core.models.transaction.TransactionPayload
import org.vibrant.core.models.tangle.TangleModel

abstract class InMemoryTangle<T: TransactionPayload, out B: TangleModel> : Tangle<T, TangleVertex<T>, B>(){

    override fun selectTips(): List<TangleVertex<T>> {
        return this.vertexes.values.map { tx ->
            tx.hash to this.vertexes.values.filter { it.parents.contains(tx.hash) }.size
        }.sortedBy { it.second }.mapNotNull { this.vertexes[it.first] }.reversed()
    }

    override fun addVertex(tip: TangleVertex<T>): Boolean {
        this.vertexes[tip.hash] = tip
        return true
    }
}