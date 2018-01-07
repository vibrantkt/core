package org.vibrant.core.base.node

import org.vibrant.core.base.models.BaseBlockChainModel
import org.vibrant.core.base.models.BaseBlockModel
import org.vibrant.core.base.models.BaseTransactionModel
import org.vibrant.core.base.producers.BaseBlockChainProducer
import org.vibrant.core.node.AbstractNode
import org.vibrant.core.node.RemoteNode
import org.vibrant.core.producers.BlockChainProducer

open class BaseNode(private val port: Int) : AbstractNode<BaseBlockChainModel, BaseBlockChainProducer>() {


    @Suppress("LeakingThis")
    private val rpc = BaseJSONRPCProtocol(this)
    internal val peer = BasePeer(port, this.rpc)

    override fun start() {
        this.peer.start()
    }

    override fun stop() {
        this.peer.stop()
    }

    override fun connect(remoteNode: RemoteNode): Boolean {
        return this.peer.connectToPeer(remoteNode)
    }



    override val chain: BaseBlockChainProducer = BaseBlockChainProducer()

}