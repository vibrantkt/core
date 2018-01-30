package org.vibrant.base

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import org.vibrant.core.node.http.HTTPPeer
import org.vibrant.core.node.http.HTTPPeerConfig
import org.vibrant.core.node.http.HTTPRequest
import org.vibrant.core.node.RemoteNode
import java.net.Socket

class TestHTTPPeer {

    val config = object: HTTPPeerConfig("rpc"){}

    private fun createPeer(onRequest: (HTTPRequest) -> ByteArray): HTTPPeer {
        var port = 5000
        while(true){
            port++
            try {
                Socket("localhost", port).close()
            }catch (e: Exception){
                val peer = object: HTTPPeer(port, config){
                    override fun handleRequest(request: HTTPRequest): ByteArray = onRequest(request)
                }
                peer.start()
                return peer
            }
        }

    }

    @Test
    fun `Test handle request`(){
        val peer1 = createPeer({ it.body })

        val body = "hello".toByteArray()
        val some = peer1.request(body, RemoteNode("localhost", peer1.port))
        assertArrayEquals(
                body,
                some
        )

        val body2 = "hello there".toByteArray()
        val some2 = peer1.request(body2, RemoteNode("localhost", peer1.port))
        assertArrayEquals(
                body2,
                some2
        )
    }

    @Test
    fun `Test init`(){
        val peer = object: HTTPPeer(7000, config){
            override fun handleRequest(request: HTTPRequest): ByteArray = request.body
        }

        assertEquals(
                7000,
                peer.port
        )
        assertEquals(
                false,
                peer.started
        )
        peer.start()
        assertEquals(
                true,
                peer.started
        )
    }
}