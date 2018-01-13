package org.vibrant.core.node

import org.vibrant.core.ModelSerializer
import org.vibrant.core.Vibrant
import org.vibrant.core.models.Model

/***
 * Abstract network node
 *
 */
abstract class AbstractNode(val vibrant: Vibrant<*, *, *>) {

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


    /**
     *
     */
    abstract fun handle(data: Model, from: RemoteNode): ByteArray


    /**
     *
     */

    fun request(data: Model, from: RemoteNode): Model{
        return vibrant.serializer.deserialize(
                this.vibrant.peer.request(vibrant.serializer.serialize(data), from)
        )
    }

    fun broadcast(data: Model): List<Model>{
        return vibrant.peer.broadcast(vibrant.serializer.serialize(data)).map { vibrant.serializer.deserialize(it) }
    }
}