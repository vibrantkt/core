package org.vibrant.core.node

import org.vibrant.core.models.Model

/***
 * Abstract network node
 *
 */
abstract class AbstractNode<T: Model> {


    /***
     * Start peer, after this node should be able to handle connections
     */
    abstract fun start()


    /***
     * Stop peer, after this node should close connections and disable
     */
    abstract fun stop()


    /**
     * Connect to peer
     */
    abstract fun connect(remoteNode: RemoteNode): Boolean



    abstract fun request(payload: T, remoteNode: RemoteNode): T


}