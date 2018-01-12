package org.vibrant.core

import org.vibrant.core.node.AbstractNode
import org.vibrant.core.node.AbstractPeer
import org.vibrant.core.node.RemoteNode

class Vibrant{

    var node: AbstractNode? = null
    var peer: AbstractPeer? = null
    var serializer: ModelSerializer? = null


    private fun check(){
        assert(node != null)
        assert(peer != null)
        assert(serializer != null)
    }

    fun start(){
        this.check()
        this.peer!!.start()
        this.node!!.start()
    }

    internal fun handleData(byteArray: ByteArray, from: RemoteNode){
        this.node?.handle(serializer!!.deserialize(byteArray), from)
    }
}