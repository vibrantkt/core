package org.vibrant.core.node


import kotlinx.coroutines.experimental.*
import mu.KotlinLogging
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.net.SocketException
import java.util.concurrent.CountDownLatch

abstract class UDPSessionPeer<Package: UDPSessionPeer.Communication.CommunicationPackage>
(port: Int, private val deserializer: UDPSessionPeer.Communication.CommunicationPackageDeserializer<Package>): AbstractPeer<RemoteNode>(port) {

    protected val logger = KotlinLogging.logger {}
    /**
     * Socket to receive packages
     */
    private val socket = DatagramSocket(this.port)


    val sessions = hashMapOf<Long, Session<Package>>()
    private var serverSession: Deferred<Unit>? = null

    val peers = arrayListOf<RemoteNode>()
    val miners = arrayListOf<RemoteNode>()


    override fun start() {
        this.serverSession = async(newSingleThreadContext("Request loop on port $port")){
            while(true){
                try{
                    val buf = ByteArray(65536)
                    val packet = DatagramPacket(buf, buf.size)
                    logger.info { "Waiting packet..." }
                    socket.receive(packet)
                    logger.info { "Received packet..." }
                    this@UDPSessionPeer.addUniqueRemoteNode(RemoteNode(packet.address.hostName, packet.port))
                    async {
                        logger.info { "Hey there ints HANDLER" }
                        this@UDPSessionPeer.handlePackage(deserializer.fromByteArray(packet.data), this@UDPSessionPeer, RemoteNode(packet.address.hostName, packet.port))
                    }
                }catch (e: SocketException){
                    break
                }
            }
        }

    }

    abstract suspend fun handlePackage(pckg: Package, peer: UDPSessionPeer<Package>, remoteNode: RemoteNode)

    private fun addUniqueRemoteNode(remoteNode: RemoteNode, miner: Boolean = false) {
        if (this.peers.find { it.address == remoteNode.address && it.port == remoteNode.port } == null) {
            this.peers.add(remoteNode)
        }
        if (miner && this.miners.find { it.address == remoteNode.address && it.port == remoteNode.port } == null) {
            this.miners.add(remoteNode)
        }
    }

    open suspend fun request(remoteNode: RemoteNode, data: Package): Package {
        val byteData = data.toByteArray()
        socket.send(DatagramPacket(byteData, byteData.size, InetSocketAddress(remoteNode.address, remoteNode.port)))
        val latch = CountDownLatch(1)
        var awaitingResponse: Package? = null
        this.sessions[data.id] = object: Session<Package>(remoteNode, data){
            override fun handle(response: Package) {
                latch.countDown()
                awaitingResponse = response
            }
        }
        latch.await()
        return awaitingResponse!!
    }

    open suspend fun send(remoteNode: RemoteNode, data: Package) {
        val byteData = data.toByteArray()
        socket.send(DatagramPacket(byteData, byteData.size, InetSocketAddress(remoteNode.address, remoteNode.port)))
    }

    open suspend fun receive(remoteNode: RemoteNode): Package {
        val buf = ByteArray(65536)
        val packet = DatagramPacket(buf, buf.size)
        socket.receive(packet)
        return deserializer.fromByteArray(packet.data)
    }


    suspend fun block(){
        this.serverSession?.await()
    }


    override fun stop() {
        this.socket.close()
        this.socket.disconnect()
        this.serverSession?.cancel()
    }



    class Communication{
        abstract class CommunicationPackage(open val id: Long){
            abstract fun toByteArray(): ByteArray
        }

        abstract class CommunicationPackageDeserializer<out Package: CommunicationPackage>{
            abstract fun fromByteArray(byteArray: ByteArray): Package
        }
    }
}