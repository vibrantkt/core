package org.vibrant.core.base.node

import org.vibrant.core.base.jsonrpc.JSONRPCRequest
import org.vibrant.core.base.jsonrpc.JSONRPCResponse

open class BaseJSONRPCProtocol(val node: BaseNode) {


    fun setConnected(){
//        node.peer.
    }



    @JSONRPCMethod
    fun echo(request: JSONRPCRequest): JSONRPCResponse<*> {
        return JSONRPCResponse(
                result = request.params[0],
                error = null,
                id = request.id
        )
    }

    fun invoke(request: JSONRPCRequest): JSONRPCResponse<*> {
        return this::class.java.getMethod(request.method, JSONRPCRequest::class.java).invoke(this, request) as JSONRPCResponse<*>
    }
}