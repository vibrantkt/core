package org.vibrant.core

import org.vibrant.core.node.AbstractNode
import org.vibrant.core.node.AbstractPeer
import org.vibrant.core.node.RemoteNode
import org.vibrant.core.producers.BlockChainProducer

class Vibrant{

    var node: AbstractNode? = null
    var peer: AbstractPeer? = null
    var serializer: ModelSerializer? = null
    var blockchain: BlockChainProducer<*>? = null


    private fun check(){
        assert(node != null)
        assert(peer != null)
        assert(serializer != null)
        assert(blockchain != null)
    }

    fun start(){
        this.check()
        this.peer!!.start()
        this.node!!.start()
    }

    internal fun handleData(byteArray: ByteArray, from: RemoteNode): ByteArray{
        return this.node!!.handle(serializer!!.deserialize(byteArray), from)
    }
}