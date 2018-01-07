package org.vibrant.core.base.node

import kotlinx.coroutines.experimental.runBlocking
import mu.KotlinLogging
import org.vibrant.core.base.BaseJSONSerializer
import org.vibrant.core.base.jsonrpc.JSONRPCRequest
import org.vibrant.core.base.models.BaseBlockChainModel
import org.vibrant.core.base.models.BaseBlockModel
import org.vibrant.core.base.models.BaseTransactionModel
import org.vibrant.core.base.producers.BaseBlockChainProducer
import org.vibrant.core.node.AbstractNode
import org.vibrant.core.node.RemoteNode
import org.vibrant.core.producers.BlockChainProducer

open class BaseNode(private val port: Int) : AbstractNode<BaseBlockChainModel, BaseBlockChainProducer>() {
    private var requestID = 0L
    private val logger = KotlinLogging.logger {  }

    @Suppress("LeakingThis") internal val rpc = BaseJSONRPCProtocol(this)
    internal val peer = BasePeer(port, this)

    internal val possibleAheads = arrayListOf<RemoteNode>()

    override fun start() {
        this.peer.start()
    }

    override fun stop() {
        this.peer.stop()
    }


    fun synchronize(remoteNode: RemoteNode){
        runBlocking {
            logger.info { "Requesting and waiting for response get last block" }
            val response = this@BaseNode.peer.send(remoteNode, JSONRPCRequest("getLastBlock", arrayOf(), requestID++)).await()
            val lastBlock = BaseJSONSerializer().deserialize(response.result.toString()) as BaseBlockModel
            val latestBlock = this@BaseNode.chain.latestBlock()
            if(latestBlock != lastBlock){
                logger.info { "My chain is not in sync with peer $remoteNode" }
                when {
                    lastBlock.index > latestBlock.index -> {
                        logger.info { "My chain is behind, requesting full chain" }
                        val chainResponse = this@BaseNode.peer.send(remoteNode, JSONRPCRequest("getFullChain", arrayOf(), requestID++)).await()
                        val model = BaseJSONSerializer().deserialize(chainResponse.result.toString()) as BaseBlockChainModel
                        val tmpChain = BaseBlockChainProducer.instantiate(
                                model
                        )

                        val chainOK = tmpChain.checkIntegrity()
                        if(chainOK){
                            logger.info { "Received chain is fine, replacing" }
                            this@BaseNode.chain.dump(model)
                            logger.info { "Received chain is fine, replaced" }
                        }else{
                            logger.info { "Received chain is not fine, I ignore it" }
                        }
                    }
                    lastBlock.index == latestBlock.index -> {
                        logger.info { "My chain is same. Just leave it, i guess" }
                    }
                    else -> {
                        val response = this@BaseNode.peer.send(remoteNode, JSONRPCRequest("syncWithMe", arrayOf(), requestID++)).await()
                        logger.info { "Wow i request sync with me $response" }
                    }
                }
            }else{
                println(latestBlock)
                println(lastBlock)
                logger.info { "Chain in sync with peer $remoteNode" }
            }
        }
    }



    override val chain: BaseBlockChainProducer = BaseBlockChainProducer()


    override fun connect(remoteNode: RemoteNode): Boolean {
        return runBlocking {
            val response = this@BaseNode.peer.send(remoteNode, JSONRPCRequest("echo", arrayOf("peer"), this@BaseNode.requestID++)).await()
            return@runBlocking if(response.result == "peer"){
                this@BaseNode.peer.addUniqueRemoteNode(remoteNode)
                true
            }else{
                false
            }
        }
    }
}