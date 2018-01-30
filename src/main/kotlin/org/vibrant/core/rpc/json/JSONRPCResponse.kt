package org.vibrant.core.rpc.json

import org.vibrant.core.rpc.RPCRequest
import org.vibrant.core.rpc.RPCResponse

data class JSONRPCResponse<out T>(
        val result: T?,
        val error: SimpleJSONRPCError?,
        val id: Long,
        val version: String = "2.0"
): JSONRPCEntity(id), RPCResponse