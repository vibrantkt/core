package org.vibrant.core.base.node

import org.vibrant.core.base.jsonrpc.JSONRPCEntity
import org.vibrant.core.base.jsonrpc.JSONRPCRequest

class BaseSession(
        val request: (JSONRPCRequest) -> JSONRPCRequest
)