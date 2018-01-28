package org.vibrant.base.http

import javax.servlet.http.HttpServletRequest

class Request(
        val request: HttpServletRequest,
        val headers: Map<String, String>,
        val body: ByteArray
)