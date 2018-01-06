package org.vibrant.core.base.node

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import org.vibrant.core.base.BaseJSONSerializer
import org.vibrant.core.base.jsonrpc.JSONRPCRequest
import org.vibrant.core.node.AbstractPeer
import org.vibrant.core.node.RemoteNode
import java.net.DatagramPacket
import java.net.DatagramSocket

class BasePeer(port: Int, private val rpc: BaseJSONRPCProtocol): AbstractPeer<RemoteNode>(port) {


    /**
     * Socket to receive packages
     */
    private val socket = DatagramSocket(this.port)
    private var running = false

    private var serverSession: Deferred<Any>? = null


    private val sessions = arrayListOf<BaseSession>()



    override fun start() {
        this.serverSession = async{
            while(true){
                val buf = ByteArray(65536)
                val packet = DatagramPacket(buf, buf.size)
                socket.receive(packet)
                val request = BaseJSONSerializer().deserializeJSONRPC(String(buf)) as JSONRPCRequest
                val response = rpc.invoke(request)
                val responseBytes = BaseJSONSerializer().serialize(response).toByteArray()
                socket.send(DatagramPacket(responseBytes, responseBytes.size, packet.socketAddress))
            }
        }

    }


    fun broadcast(request: JSONRPCRequest){

    }

    override fun stop() {
        this.serverSession?.cancel()
    }

    override fun connectToPeer(remoteNode: RemoteNode) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}