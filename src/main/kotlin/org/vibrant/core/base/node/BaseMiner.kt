package org.vibrant.core.base.node

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.vibrant.core.base.BaseJSONSerializer
import org.vibrant.core.base.jsonrpc.JSONRPCRequest
import org.vibrant.core.base.models.BaseBlockModel
import org.vibrant.core.base.models.BaseTransactionModel
import org.vibrant.core.base.producers.BaseBlockProducer
import org.vibrant.core.node.RemoteNode
import java.util.*
import java.util.concurrent.CountDownLatch

class BaseMiner(port: Int) : BaseNode(port){


    private var latch = CountDownLatch(1)

    private val pendingTransactions = arrayListOf<BaseTransactionModel>()


    internal val onMined = arrayListOf<(BaseBlockModel) -> Unit>()

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
        logger.info { "Block mined" }
        this.pendingTransactions.clear()
        runBlocking {
            logger.info { "Broadcasting this block..." }
            val response = this@BaseMiner.peer.broadcast(JSONRPCRequest(
                    method = "newBlock",
                    params = arrayOf(BaseJSONSerializer().serialize(block)),
                    id = this@BaseMiner.requestID++
            ))
            logger.info { "Awaited this shit! $response" }
        }
        this.onMined.forEach { it(block) }
    }


    internal fun startMineLoop() {
        while(true){
            latch.await()
            this@BaseMiner.mine()
            latch = CountDownLatch(1)
        }
    }
}