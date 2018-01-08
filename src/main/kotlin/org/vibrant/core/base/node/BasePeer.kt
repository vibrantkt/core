package org.vibrant.core.base.node

import kotlinx.coroutines.experimental.*
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

class BasePeer(port: Int, private val node: BaseNode): AbstractPeer<RemoteNode>(port) {

    val logger = KotlinLogging.logger {  }
    /**
     * Socket to receive packages
     */
    private val socket = DatagramSocket(this.port)

    private var serverSession: Deferred<Any>? = null

    internal val sessions = hashMapOf<Long, BaseSession>()


    internal val allPeers = arrayListOf<RemoteNode>()
    internal val miners = arrayListOf<RemoteNode>()

    internal val onRequestReceivedListeners = arrayListOf<(JSONRPCRequest) -> Unit>()
    internal val onResponseReceivedListeners = arrayListOf<(JSONRPCResponse<*>) -> Unit>()


    override fun start() {
        this.serverSession = async{
            while(true){
                val buf = ByteArray(65536)
                val packet = DatagramPacket(buf, buf.size)
                logger.info { "Waiting JSON RPC 2.0 request... $port" }
                socket.receive(packet)
                this@BasePeer.addUniqueRemoteNode(RemoteNode(packet.address.hostName, packet.port))
                //idk why coroutine ain't working here.
                Thread{
                    runBlocking {
                        this@BasePeer.handleResponse(buf, packet)
                    }
                }.start()

                logger.info { "Suspended handler" }
            }
        }

    }

    private suspend fun handleResponse(buf: ByteArray, packet: DatagramPacket){
        val entity = BaseJSONSerializer().deserializeJSONRPC(String(buf))
        when (entity) {
            is JSONRPCRequest -> {
                logger.info { "Received request $entity" }
                val response = node.rpc.invoke(entity, RemoteNode(packet.address.hostName, packet.port))
                logger.info { "Responding with $response" }
                val responseBytes = BaseJSONSerializer().serialize(response).toByteArray()
                socket.send(DatagramPacket(responseBytes, responseBytes.size, packet.socketAddress))
                this@BasePeer.node.possibleAheads.forEach {
                    logger.info { "possible ahead, syncing.." }
                    this@BasePeer.node.synchronize(it)
                    logger.info { "possible ahead synced!" }
                }
                this@BasePeer.node.possibleAheads.clear()
                this@BasePeer.onRequestReceivedListeners.forEach { it(entity) }
            }
            is JSONRPCResponse<*> -> {
                logger.info { "Received response $entity, handling..." }
                this@BasePeer.sessions[entity.id]?.handle(entity)
                this@BasePeer.onResponseReceivedListeners.forEach { it(entity) }
            }
            else -> {

            }
        }
    }

    internal fun addUniqueRemoteNode(remoteNode: RemoteNode, miner: Boolean = false) {
        if (this.allPeers.find { it.address == remoteNode.address && it.port == remoteNode.port } == null) {
            this.allPeers.add(remoteNode)
        }
        if (miner && this.miners.find { it.address == remoteNode.address && it.port == remoteNode.port } == null) {
            this.miners.add(remoteNode)
        }
    }


    fun broadcast(request: JSONRPCRequest): List<Deferred<JSONRPCResponse<*>>> {
        return this.allPeers.map {
            this@BasePeer.send(it, request)
        }
    }

    fun send(remoteNode: RemoteNode, request: JSONRPCRequest): Deferred<JSONRPCResponse<*>> {
        val serialized = BaseJSONSerializer().serialize(request).toByteArray()
        val latch = CountDownLatch(1)
        var deferredResponse: JSONRPCResponse<*>? = null
        this@BasePeer.sessions[request.id] = object : BaseSession(remoteNode, request){
            override fun handle(response: JSONRPCResponse<*>) {
                deferredResponse = response
                this@BasePeer.sessions.remove(request.id)
                latch.countDown()
            }
        }

        return async {
            socket.send(DatagramPacket(serialized, serialized.size, InetSocketAddress(remoteNode.address, remoteNode.port)))
            latch.await()
            deferredResponse!!
        }

    }



    override fun stop() {
        this.serverSession?.cancel()
        this.socket.close()
    }

}