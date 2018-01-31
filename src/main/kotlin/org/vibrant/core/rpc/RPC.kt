package org.vibrant.core.rpc

import org.vibrant.core.node.RemoteNode

abstract class RPC<in Req: RPCRequest, out Res: RPCResponse>{
    abstract fun invoke(request: Req, remoteNode: RemoteNode): Res
}