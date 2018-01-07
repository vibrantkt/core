package org.vibrant.core.base.node

import org.vibrant.core.base.BaseJSONSerializer
import org.vibrant.core.base.jsonrpc.JSONRPCRequest
import org.vibrant.core.base.jsonrpc.JSONRPCResponse
import org.vibrant.core.base.models.BaseTransactionModel

open class BaseJSONRPCProtocol(val node: BaseNode) {


    @JSONRPCMethod
    fun addTransaction(request: JSONRPCRequest): JSONRPCResponse<*>{
        val transaction = BaseJSONSerializer().deserialize(request.params[0].toString()) as BaseTransactionModel
        if(node is BaseMiner){

        }
        return JSONRPCResponse(
                result = node is BaseMiner,
                error = null,
                id = request.id
        )
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