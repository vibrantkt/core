package org.vibrant.base.http

import org.vibrant.base.rpc.WrongProtocolException
import org.vibrant.base.rpc.json.JSONRPC
import org.vibrant.base.rpc.json.JSONRPCRequest
import org.vibrant.base.rpc.json.JSONRPCSerializer
import org.vibrant.core.node.RemoteNode
import org.vibrant.base.rpc.json.JSONRPCResponse

@Suppress("unused")
open class HTTPJsonRPCPeer(port: Int, private val rpc: JSONRPC, config: HTTPPeerConfig = object: HTTPPeerConfig(endpoint = "/rpc"){}): HTTPPeer(port, config) {




    @Suppress("MemberVisibilityCanBePrivate")
    val peers = arrayListOf<RemoteNode>()


    override fun handleRequest(request: Request): ByteArray {
        val remotePort = request.headers["peer-port"]?.toInt()
        if(remotePort != null){
            val remoteNode = RemoteNode(request.request.remoteAddr, remotePort)

            this.addUniqueRemoteNode(remoteNode)

            val jsonRPCRequest = JSONRPCSerializer.deserialize(request.body) as JSONRPCRequest
            val response = rpc.invoke(jsonRPCRequest, remoteNode)
            return JSONRPCSerializer.serialize(response)
        }else {
            throw WrongProtocolException("Expected 'peer-port' header")
        }
    }


    fun request(remoteNode: RemoteNode, jsonrpcRequest: JSONRPCRequest): JSONRPCResponse<*> {
        val response = this.request(JSONRPCSerializer.serialize(jsonrpcRequest), remoteNode)
        return JSONRPCSerializer.deserialize(response) as JSONRPCResponse<*>
    }


    fun broadcast(request: JSONRPCRequest, peers: List<RemoteNode> = this.peers): List<JSONRPCResponse<*>> {
        return peers.toList().map{
            this.request(it, request)
        }
    }

    open fun addUniqueRemoteNode(remoteNode: RemoteNode) {
        if (this.peers.toList().find { it.address == remoteNode.address && it.port == remoteNode.port } == null) {
            synchronized(this.peers){
                this.peers.add(remoteNode)
            }
        }
    }

}