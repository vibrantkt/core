package org.vibrant.base.database.tangle

import org.vibrant.base.database.blockchain.models.TransactionPayload
import org.vibrant.base.database.tangle.models.TangleModel
import org.vibrant.core.ModelProducer

abstract class Tangle<out B: TransactionPayload, T: TangleVertex<B>, out M: TangleModel>: ModelProducer<M>() {

    /**
     * Key = tx ID
     * Value = TX
     */
    protected val vertexes = hashMapOf<String, T>()

    protected val rootVertex: TangleVertex<B> = this.createGenesisVertex()



    init {
        val genesis = this.createGenesisVertex()
        this.vertexes[genesis.hash] = genesis
    }


    /**
     * Select tips to create next vertex
     */
    abstract fun selectTips(): List<T>

    /**
     * Add vertex to dag
     */
    abstract fun addVertex(tip: T): Boolean

    /**
     * Return constant genesis vertex
     */
    abstract fun createGenesisVertex(): T


    fun getParents(tip: T): List<T> = tip.parents.mapNotNull { this.vertexes[it] }
}