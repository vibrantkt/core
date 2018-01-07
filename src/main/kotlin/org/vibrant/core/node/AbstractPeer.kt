package org.vibrant.core.node

abstract class AbstractPeer<in T: RemoteNode>(val port: Int){

    /**
     * Stop peer
     */
    abstract fun stop()

    /**
     * Start peer
     */
    abstract fun start()

}