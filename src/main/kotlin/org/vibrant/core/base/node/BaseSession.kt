package org.vibrant.core.base.node

import org.vibrant.core.base.jsonrpc.JSONRPCEntity
import org.vibrant.core.base.jsonrpc.JSONRPCRequest
import org.vibrant.core.base.jsonrpc.JSONRPCResponse
import org.vibrant.core.node.RemoteNode

abstract class BaseSession(val remoteNode: RemoteNode, val request: JSONRPCRequest){
    abstract fun handle(response: JSONRPCResponse<*>)
}