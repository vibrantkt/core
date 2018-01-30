package org.vibrant.core.node.http

import javax.servlet.http.HttpServletRequest

class HTTPRequest(
        val request: HttpServletRequest,
        val headers: Map<String, String>,
        val body: ByteArray
)