package org.vibrant.core.node

abstract class AbstractPeer{

    /**
     * Stop peer
     */
    abstract fun stop()

    /**
     * Start peer
     */
    abstract fun start()


    abstract fun request(byteArray: ByteArray, remoteNode: RemoteNode): ByteArray


}