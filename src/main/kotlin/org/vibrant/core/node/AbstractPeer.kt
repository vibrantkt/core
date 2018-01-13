package org.vibrant.core.node

import org.vibrant.core.Vibrant

abstract class AbstractPeer(val vibrant: Vibrant<*, *, *>, val port: Int){


    val peers: ArrayList<RemoteNode> = arrayListOf()


    /**
     * Stop peer
     */
    abstract fun stop()

    /**
     * Start peer
     */
    abstract fun start()


    protected fun handleData(byteArray: ByteArray, from: RemoteNode): ByteArray {
        return this.vibrant.handleData(byteArray, from)
    }



    abstract fun request(byteArray: ByteArray, from: RemoteNode): ByteArray


    open fun addUniqueRemoteNode(remoteNode: RemoteNode, miner: Boolean = false) {
        if (this.peers.find { it.address == remoteNode.address && it.port == remoteNode.port } == null) {
            this.peers.add(remoteNode)
        }
    }


    fun broadcast(byteArray: ByteArray): List<ByteArray> {
        return this.peers.map{
            this.request(byteArray, it)
        }
    }

}