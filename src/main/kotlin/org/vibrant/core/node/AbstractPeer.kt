package org.vibrant.core.node

import org.vibrant.core.Vibrant

abstract class AbstractPeer(val vibrant: Vibrant, val port: Int){

    /**
     * Stop peer
     */
    abstract fun stop()

    /**
     * Start peer
     */
    abstract fun start()


    protected fun handleData(byteArray: ByteArray, from: RemoteNode){
        this.vibrant.handleData(byteArray, from)
    }

}