package org.vibrant.core

import org.vibrant.core.node.AbstractNode
import org.vibrant.core.node.AbstractPeer
import org.vibrant.core.node.RemoteNode

open class Vibrant<N: AbstractNode, P: AbstractPeer, S: ModelSerializer>(
        val node: N,
        val peer: P,
        val serializer: S
){

    open fun start(){
        this.peer.start()
        this.node.start()
    }


    open fun stop(){
        this.peer.stop()
        this.node.stop()
    }

    internal fun handleData(byteArray: ByteArray, from: RemoteNode): ByteArray{
        return this.node.handle(serializer.deserialize(byteArray), from)
    }
}