package org.vibrant.core.node

import org.vibrant.core.models.BlockChainModel
import org.vibrant.core.models.BlockModel
import org.vibrant.core.producers.BlockChainProducer

/***
 * Abstract network node
 *
 * @property chain blockchain
 */
abstract class AbstractNode<T: BlockChainModel, out B: BlockChainProducer<T>> {

    internal abstract val chain: B


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

}