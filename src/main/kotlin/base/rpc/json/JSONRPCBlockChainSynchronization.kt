package org.vibrant.base.rpc.json

import mu.KLogger
import org.vibrant.base.database.blockchain.InMemoryBlockChain
import org.vibrant.base.database.blockchain.InstantiateBlockChain
import org.vibrant.base.http.HTTPJsonRPCPeer
import org.vibrant.base.node.JSONRPCNode
import org.vibrant.base.rpc.JSONRPCMethod
import org.vibrant.base.rpc.json.JSONRPCRequest
import org.vibrant.base.rpc.json.JSONRPCResponse
import org.vibrant.core.ConcreteModelSerializer
import org.vibrant.core.node.RemoteNode

interface JSONRPCBlockChainSynchronization<T: HTTPJsonRPCPeer,
        B: org.vibrant.base.database.blockchain.models.ClassicBlock,
        TX: org.vibrant.base.database.blockchain.models.HashedTransaction,
        BC: org.vibrant.base.database.blockchain.models.BlockChainModel> {

    val chain: InMemoryBlockChain<B, BC>
    val node: JSONRPCNode<T>
    val logger: KLogger
    val modelToProducer: InstantiateBlockChain<B, BC>

    val broadcastedTransactions: ArrayList<String>
    val broadcastedBlocks: ArrayList<String>

    val chainSerializer: ConcreteModelSerializer<BC>
    val transactionSerializer: ConcreteModelSerializer<TX>
    val blockSerializer: ConcreteModelSerializer<B>


    fun onLastBlock(lastBlock: B, remoteNode: RemoteNode): Boolean {
        val localLatestBlock = this.chain.latestBlock()
        if(localLatestBlock != lastBlock){
            when {
            // next block
                lastBlock.index - localLatestBlock.index == 1L && lastBlock.previousHash == localLatestBlock.hash -> {
                    this.chain.addBlock(
                            lastBlock
                    )
                }
            // block is ahead
                lastBlock.index > localLatestBlock.index -> {
                    val chainResponse = this.node.request(
                            this.node.createRequest("getFullChain", arrayOf()),
                            remoteNode
                    )

                    val model = chainSerializer.deserialize(chainResponse.result.toString().toByteArray())

                    val tmpChain = modelToProducer.asBlockChainProducer(model)
                    val chainOK = tmpChain.checkIntegrity()

                    if(chainOK){
                        logger.info { "Received chain is fine, replacing" }
                        this.chain.dump(model)
                        logger.info { "Received chain is fine, replaced" }
                    }else{
                        logger.info { "Received chain is not fine, I ignore it" }
                    }
                }
            // block is behind
                else -> {
                    logger.info { "My chain is ahead, sending request" }
                    val response = this.node.request(this.node.createRequest("syncWithMe", arrayOf()), remoteNode)
                    logger.info { "Got response! $response" }
                }
            }
            return false
        }else{
            logger.info { "node.Chain in sync with peer $remoteNode" }
            return true
        }
    }

    @JSONRPCMethod
    fun onNewBlock(request: JSONRPCRequest, remoteNode: RemoteNode): JSONRPCResponse<*> {
        @Suppress("UNCHECKED_CAST")
        val block = this.blockSerializer.deserialize(request.params[0].toString().toByteArray())
        logger.info { "Received last block, handling.." }
        val result = this.onLastBlock(block, remoteNode)

        if(!broadcastedBlocks.contains(block.hash)){
            broadcastedBlocks.add(block.hash)
            logger.info { "$result - true => block already attached and i won't share" }
            val some = this.node.peer.broadcast(request, this.node.peer.peers.filter { it.address != remoteNode.address || it.port != remoteNode.port })
            logger.info { "Broad casted between connected nodes $some" }
        }
        return JSONRPCResponse(result, null, request.id)
    }



    @JSONRPCMethod
    fun getLastBlock(request: JSONRPCRequest, remoteNode: RemoteNode): JSONRPCResponse<*>{
        return JSONRPCResponse(
                String(this.chainSerializer.serialize(this.chain.latestBlock())),
                null,
                request.id
        )
    }


    @JSONRPCMethod
    fun getFullChain(request: JSONRPCRequest, remoteNode: RemoteNode): JSONRPCResponse<*>{
        logger.info { "Requested full chain, responding..." }
        return JSONRPCResponse(
                String(this.chainSerializer.serialize(this.chain.produce(this.chainSerializer))),
                null,
                request.id
        )
    }


    @Suppress("UNCHECKED_CAST")
    fun synchronize(remoteNode: RemoteNode){
        val lastBlock = this.node.request(
                this.node.createRequest("getLastBlock", arrayOf()),
                remoteNode
        )
        val block = this.blockSerializer.deserialize(lastBlock.result.toString().toByteArray())
        this.onLastBlock(block, remoteNode)
    }


    @JSONRPCMethod
    fun addTransaction(request: JSONRPCRequest, remoteNode: RemoteNode): JSONRPCResponse<*>{
        val transaction = this.transactionSerializer.deserialize(request.params[0].toString().toByteArray())
        if(!this.broadcastedTransactions.contains(transaction.hash)) {
            this.broadcastedTransactions.add(transaction.hash)
            this.node.peer.broadcast(request, this.node.peer.peers.filter { it.address != remoteNode.address || it.port != remoteNode.port })
            this.handleDistinctTransaction(transaction)
        }
        return JSONRPCResponse(true, null, request.id)
    }






    @JSONRPCMethod
    fun syncWithMe(request: JSONRPCRequest, remoteNode: RemoteNode): JSONRPCResponse<*>{
        this.synchronize(remoteNode)
        return JSONRPCResponse(
                true,
                null,
                request.id
        )
    }

    fun handleDistinctTransaction(transaction: TX)
}