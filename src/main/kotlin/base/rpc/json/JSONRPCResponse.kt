package org.vibrant.base.rpc.json

data class JSONRPCResponse<out T>(
        val result: T?,
        val error: SimpleJSONRPCError?,
        val id: Long,
        val version: String = "2.0"
): JSONRPCEntity(id)