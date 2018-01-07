package org.vibrant.core.base.node

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import mu.KotlinLogging
import org.vibrant.core.base.BaseJSONSerializer
import org.vibrant.core.base.jsonrpc.JSONRPCRequest
import org.vibrant.core.base.jsonrpc.JSONRPCResponse
import org.vibrant.core.node.AbstractPeer
import org.vibrant.core.node.RemoteNode
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.util.concurrent.CountDownLatch

class BasePeer(port: Int, private val rpc: BaseJSONRPCProtocol): AbstractPeer<RemoteNode>(port) {

    val logger = KotlinLogging.logger {  }
    /**
     * Socket to receive packages
     */
    private val socket = DatagramSocket(this.port)

    private var serverSession: Deferred<Any>? = null

    private val sessions = hashMapOf<String, BaseSession>()


    internal val peers = arrayListOf<RemoteNode>()


    override fun start() {
        this.serverSession = async{
            while(true){
                val buf = ByteArray(65536)
                val packet = DatagramPacket(buf, buf.size)
                logger.info { "Waiting JSON RPC 2.0 request..." }
                socket.receive(packet)
                this@BasePeer.addUniqueRemoteNode(RemoteNode(packet.address.hostName, packet.port))
                // response in async
                val entity = BaseJSONSerializer().deserializeJSONRPC(String(buf))
                when(entity){
                    is JSONRPCRequest -> {
                        logger.info { "Received request $entity" }
                        val response = rpc.invoke(entity)
                        val responseBytes = BaseJSONSerializer().serialize(response).toByteArray()
                        socket.send(DatagramPacket(responseBytes, responseBytes.size, packet.socketAddress))
                    }
                    is JSONRPCResponse<*> -> {
                        logger.info { "Received response $entity, handling..." }
                        this@BasePeer.sessions[entity.id]?.handle(entity)
                    }
                }
                async{

                }
            }
        }

    }

    private fun addUniqueRemoteNode(remoteNode: RemoteNode) {
        if (this.peers.find { it.address == remoteNode.address && it.port == remoteNode.port } == null) {
            this.peers.add(remoteNode)
        }
    }


    fun broadcast(request: JSONRPCRequest): List<Deferred<JSONRPCResponse<*>>> {
        return this.peers.map {
            this@BasePeer.send(it, request)
        }
    }

    fun send(remoteNode: RemoteNode, request: JSONRPCRequest): Deferred<JSONRPCResponse<*>> {
        val serialized = BaseJSONSerializer().serialize(request).toByteArray()

        return async {
            val latch = CountDownLatch(1)
            var deferredResponse: JSONRPCResponse<*>? = null
            this@BasePeer.sessions[request.id] = object : BaseSession(remoteNode, request){
                override fun handle(response: JSONRPCResponse<*>) {
                    deferredResponse = response
                    latch.countDown()
                }
            }
            socket.send(DatagramPacket(serialized, serialized.size, InetSocketAddress(remoteNode.address, remoteNode.port)))
            latch.await()
            deferredResponse!!
        }

    }

    override fun stop() {
        this.serverSession?.cancel()
        this.socket.close()
    }

    override fun connectToPeer(remoteNode: RemoteNode): Boolean {
        return runBlocking {
            val response = this@BasePeer.send(remoteNode, JSONRPCRequest("echo", arrayOf("peer"), "1")).await()
            return@runBlocking if(response.result == "peer"){
                this@BasePeer.addUniqueRemoteNode(remoteNode)
                true
            }else{
                false
            }
        }
    }
}