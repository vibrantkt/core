package org.vibrant.core

import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.vibrant.core.node.RemoteNode
import org.vibrant.core.node.UDPSessionPeer
import java.net.SocketException

class TestUDPPeer {

    data class ExamplePackage(override val id: Long, val payload: String): UDPSessionPeer.Communication.CommunicationPackage(id) {
        override fun toByteArray(): ByteArray {
            return "$id\n$payload".toByteArray(charset("UTF-8"))
        }
    }

    class Deserializer : UDPSessionPeer.Communication.CommunicationPackageDeserializer<ExamplePackage>(){
        override fun fromByteArray(byteArray: ByteArray): ExamplePackage {

            var i = 0
            while (i < byteArray.size && byteArray[i] != (0).toByte()) {
                i++
            }
            val str = String(byteArray, 0, i, charset("UTF-8")).split(Regex("\n"), 2)

            return ExamplePackage(str[0].toLong(), str[1])
        }

    }

    class SimpleImplementation(port: Int): UDPSessionPeer<ExamplePackage>(port, Deserializer()){
        override suspend fun handlePackage(pckg: ExamplePackage, peer: UDPSessionPeer<ExamplePackage>, remoteNode: RemoteNode) {
            if(peer.sessions.containsKey(pckg.id)){
                this.sessions[pckg.id]?.handle(pckg)
                this.sessions.remove(pckg.id)
            }else{
                peer.send(remoteNode, ExamplePackage(pckg.id, pckg.payload))
            }
        }

    }

    @Test
    fun `Test echo`(){

        val node1 = SimpleImplementation(7001)
        val node2 = SimpleImplementation(7002)

        node1.start()
        node2.start()


        runBlocking {
            val some = node1.request(RemoteNode("localhost", 7002), ExamplePackage(0, "Hello"))

            assertEquals(
                    "Hello",
                    some.payload
            )

            val some2 = node1.request(RemoteNode("localhost", 7002), ExamplePackage(1, "Bye"))

            assertEquals(
                    "Bye",
                    some2.payload
            )

        }


        node1.stop()
        node2.stop()

        //test closing udp session
        runBlocking {
            val response = try{
                node1.request(RemoteNode("localhost", 7002), ExamplePackage(1, "Bye"))
            }catch (e: SocketException){
                null
            }

            assertEquals(
                    null,
                    response
            )
        }
    }

}