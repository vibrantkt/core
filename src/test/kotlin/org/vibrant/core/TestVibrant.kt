package org.vibrant.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.vibrant.core.models.Model
import org.vibrant.core.node.AbstractNode
import org.vibrant.core.node.AbstractPeer
import org.vibrant.core.node.RemoteNode

class TestVibrant {


    @Test
    fun `Combine`(){

        val vibrant = Vibrant().apply {
            node = object: AbstractNode(this){
                override fun handle(data: Model, from: RemoteNode) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun start() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun stop() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun connect(remoteNode: RemoteNode): Boolean {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }
            peer = object: AbstractPeer(this, 9999){

                override fun start() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun stop() {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }
            serializer = object: ModelSerializer(){
                override fun deserialize(serialized: ByteArray): Model {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun serialize(model: Model): ByteArray {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            }
        }

        assertNotEquals(
                null,
                vibrant.node
        )

        assertNotEquals(
                null,
                vibrant.peer
        )

        assertNotEquals(
                null,
                vibrant.serializer
        )
    }
}