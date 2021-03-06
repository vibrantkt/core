package org.vibrant.core.node

import mu.KotlinLogging
import org.vibrant.core.node.http.HTTPJsonRPCPeer
import org.vibrant.core.rpc.json.JSONRPCEntity
import org.vibrant.core.rpc.json.JSONRPCRequest
import org.vibrant.core.rpc.json.JSONRPCResponse

@Suppress("AddVarianceModifier")
abstract class JSONRPCNode<T: HTTPJsonRPCPeer>: AbstractNode<JSONRPCEntity>(){

    @Suppress("unused")
    protected val logger = KotlinLogging.logger {  }

    @Suppress("MemberVisibilityCanBePrivate")
    protected var requestID: Long = 0

    abstract val peer: T

    override fun connect(remoteNode: RemoteNode): Boolean {
        val response1 = this.request(this.createRequest("echo", arrayOf("peer")), remoteNode)
        return response1.result == "peer"
    }

    override fun request(payload: JSONRPCEntity, remoteNode: RemoteNode): JSONRPCResponse<*> {
        return this.peer.request(remoteNode, payload as JSONRPCRequest)
    }

    override fun start() {
        this.peer.start()
    }

    override fun stop() {
        this.peer.stop()
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun createRequest(method: String, params: Array<Any>): JSONRPCRequest = JSONRPCRequest(method, params, this.requestID++)


}