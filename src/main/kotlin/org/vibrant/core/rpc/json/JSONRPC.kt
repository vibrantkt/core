@file:Suppress("UNUSED_PARAMETER")

package org.vibrant.core.rpc.json

import org.vibrant.core.rpc.RPC
import org.vibrant.core.node.RemoteNode

abstract class JSONRPC: RPC<JSONRPCRequest, JSONRPCResponse<*>>() {

    override fun invoke(request: JSONRPCRequest, remoteNode: RemoteNode): JSONRPCResponse<*> {
        return try {
            val method = this::class.java.getMethod(request.method, JSONRPCRequest::class.java, RemoteNode::class.java)
            method.invoke(this, request, remoteNode) as JSONRPCResponse<*>
        }catch (e: NoSuchMethodException){
            val error = SimpleJSONRPCError(SimpleJSONRPCError.ErrorCode.METHOD_NOT_FOUND, "No such method", "")
            JSONRPCResponse(null, error, request.id)
        }catch (e: IllegalArgumentException){
            val error = SimpleJSONRPCError(SimpleJSONRPCError.ErrorCode.INVALID_PARAMETERS, "Invalid parameters", "")
            JSONRPCResponse(null, error, request.id)
        }
    }
}