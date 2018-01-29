package org.vibrant.base.rpc.json

import org.vibrant.base.rpc.JSONRPCMethod
import org.vibrant.core.node.RemoteNode

@Suppress("UNUSED_PARAMETER")
class JSONRPCDefaultProtocol: JSONRPC() {


    @JSONRPCMethod
    fun connect(request: JSONRPCRequest, remoteNode: RemoteNode): JSONRPCResponse<Boolean> {
        return JSONRPCResponse(
                result = true,
                error = null,
                id = request.id
        )
    }
}