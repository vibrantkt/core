@file:Suppress("UNUSED_PARAMETER")

package org.vibrant.base.rpc.json

import mu.KotlinLogging
import org.vibrant.base.rpc.RPC
import org.vibrant.core.node.RemoteNode

abstract class JSONRPC: RPC() {

    fun invoke(request: JSONRPCRequest, remoteNode: RemoteNode): JSONRPCResponse<*> {
        return try {
            val method = this::class.java.getMethod(request.method, JSONRPCRequest::class.java, RemoteNode::class.java)
            method.invoke(this, request, remoteNode) as JSONRPCResponse<*>
        }catch (e: Exception){
            val error = SimpleJSONRPCError(SimpleJSONRPCError.ERROR_CODE.METHOD_NOT_FOUND, "No such method", "")
            JSONRPCResponse(null, error, request.id)
        }catch (e: IllegalArgumentException){
            val error = SimpleJSONRPCError(SimpleJSONRPCError.ERROR_CODE.INVALID_PARAMETERS, "No such method", "")
            JSONRPCResponse(null, error, request.id)
        }
    }
}