package org.vibrant.base


import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.vibrant.core.node.http.HTTPJsonRPCPeer
import org.vibrant.core.node.http.HTTPPeerConfig
import org.vibrant.core.rpc.json.JSONRPCDefaultProtocol
import org.vibrant.core.rpc.json.JSONRPCRequest
import org.vibrant.core.rpc.json.SimpleJSONRPCError
import org.vibrant.core.node.RemoteNode
import java.net.Socket

class TestHTTPJsonRPCPeer {

    private val config = object: HTTPPeerConfig("rpc"){}

    private val rpc = JSONRPCDefaultProtocol()

    private fun createPeer(): HTTPJsonRPCPeer {
        var port = 5000
        while(true){
            port++
            try {
                Socket("localhost", port).close()
            }catch (e: Exception){
                val peer = HTTPJsonRPCPeer(port, rpc, config)
                peer.start()
                return peer
            }
        }

    }

    @Test
    fun `Test handle request`(){
        val peer1 = createPeer()

        val some = peer1.request(
                RemoteNode("localhost", peer1.port),
                JSONRPCRequest("connect", arrayOf(), 1L))
        assertEquals(
                true,
                some.result
        )
        assertEquals(
                null,
                some.error
        )
        assertEquals(
                1,
                some.id
        )
        assertEquals(
                "2.0",
                some.version
        )

        val errorSome = peer1.request(
                RemoteNode("localhost", peer1.port),
                JSONRPCRequest("disconnect", arrayOf(), 2L)
        )

        assertNotEquals(
                null,
                errorSome.error
        )
        assertEquals(
                SimpleJSONRPCError.ErrorCode.METHOD_NOT_FOUND,
                errorSome.error!!.code
        )


    }
}