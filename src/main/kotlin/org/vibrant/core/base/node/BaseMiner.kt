package org.vibrant.core.base.node

import org.vibrant.core.base.BaseJSONSerializer
import org.vibrant.core.base.models.BaseTransactionModel
import org.vibrant.core.base.producers.BaseBlockProducer
import java.util.*
import java.util.concurrent.CountDownLatch

class BaseMiner(port: Int) : BaseNode(port){


    private var latch = CountDownLatch(1)

    private val pendingTransactions = arrayListOf<BaseTransactionModel>()

    fun addTransaction(transactionModel: BaseTransactionModel){
        this.pendingTransactions.add(transactionModel)
        latch.countDown()
    }



    internal fun mine(){
        val timestamp = Date().time
        val block = this.chain.pushBlock(this.chain.createBlock(
                this.pendingTransactions,
                BaseJSONSerializer(),
                timestamp = timestamp
        ))
        this.pendingTransactions.clear()

    }


    internal fun startMineLoop() {
        while(true){
            latch.await()
            this@BaseMiner.mine()
            latch = CountDownLatch(1)
        }
    }
}