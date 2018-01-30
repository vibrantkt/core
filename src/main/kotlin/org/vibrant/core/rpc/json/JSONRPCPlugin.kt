package org.vibrant.core.rpc.json

import org.vibrant.core.node.RemoteNode

abstract class JSONRPCPlugin {
    abstract val handlers: HashMap<String, (JSONRPCRequest, RemoteNode) -> JSONRPCResponse<*>>
}