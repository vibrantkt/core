package org.vibrant.extend

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.vibrant.core.base.BaseJSONSerializer
import org.vibrant.core.base.jsonrpc.JSONRPCRequest
import org.vibrant.core.base.models.BaseMessageModel
import org.vibrant.core.base.node.BaseMiner
import org.vibrant.core.base.node.BaseNode
import org.vibrant.core.base.producers.BaseTransactionProducer
import org.vibrant.core.node.RemoteNode
import org.vibrant.core.reducers.SignatureProducer
import org.vibrant.core.util.AccountUtils
import org.vibrant.core.util.HashUtils
import java.security.KeyPair
import kotlin.coroutines.experimental.suspendCoroutine

class TestBasePeer {


    @Test
    fun `Test peer echo`(){
        val node = BaseNode(7000)
        val miner = BaseMiner(7001)


        node.start()
        miner.start()

        val some = node.connect(RemoteNode("localhost", 7001))
        // check ping - pong
        assertEquals(
                true,
                some
        )

        // connection established

        assertEquals(
                node.peer.allPeers.size,
                1
        )

        assertEquals(
                node.peer.miners.size,
                1
        )

        assertEquals(
                miner.peer.allPeers.size,
                1
        )

        assertEquals(
                miner.peer.miners.size,
                0
        )


        node.stop()
        miner.stop()
    }


    @Test
    fun `Test peer return type`(){
        val node1 = BaseNode(7000)
        val node2 = BaseMiner(7001)


        node1.start()
        node2.start()


        val expectedNode = node1.rpc.nodeType(JSONRPCRequest("a", arrayOf(),1L), RemoteNode("", 0))
        val expectedMiner = node2.rpc.nodeType(JSONRPCRequest("a", arrayOf(),1L), RemoteNode("", 0))

        assertEquals(
                expectedNode.result.toString(),
                "node"
        )

        assertEquals(
                expectedMiner.result.toString(),
                "miner"
        )

        node1.stop()
        node2.stop()
    }


    @Test
    fun `Test peer behind sync`(){
        val node = BaseNode(7000)
        val miner = BaseMiner(7001)


        node.start()
        miner.start()

        miner.chain.pushBlock(node.chain.createBlock(
                listOf(),
                BaseJSONSerializer()
        ))


        node.connect(RemoteNode("localhost", 7001))
        println("Connected")
        node.synchronize(RemoteNode("localhost", 7001))
        println("Synced")


        assertEquals(
                miner.chain.produce(BaseJSONSerializer()),
                node.chain.produce(BaseJSONSerializer())
        )

        miner.stop()
        node.stop()
    }

    @Test
    fun `Test peer ahead sync`(){
        val node = BaseNode(7000)
        val miner = BaseMiner(7001)


        async {
            node.start()
        }

        async {

            miner.start()
        }

        miner.chain.pushBlock(miner.chain.createBlock(
                listOf(),
                BaseJSONSerializer()
        ))




        miner.connect(RemoteNode("localhost", 7000))
        println("Connected")
        miner.synchronize(RemoteNode("localhost", 7000))
        println("Synced")

        runBlocking {
            suspendCoroutine<Unit> { s ->
                node.chain.onChange.add { _ ->
                    s.resume(Unit)
                }
            }
        }

        assertEquals(
                miner.chain.produce(BaseJSONSerializer()),
                node.chain.produce(BaseJSONSerializer())
        )

        miner.stop()
        node.stop()



    }


    @Test
    fun `Test miner loop`(){
        val miner = BaseMiner(7001)

        val sender = AccountUtils.generateKeyPair()

        val transaction = BaseTransactionProducer(
                "yura",
                "vasya",
                BaseMessageModel("Hello!", 0),
                sender,
                object : SignatureProducer {
                    override fun produceSignature(content: ByteArray, keyPair: KeyPair): ByteArray {
                        return HashUtils.signData(content, keyPair)
                    }
                }
        ).produce(BaseJSONSerializer())

        miner.addTransaction(transaction)
        miner.mine()

        val chain = miner.chain.produce(BaseJSONSerializer())

        assertEquals(
                2,
                chain.blocks.size
        )

        assertEquals(
                "0",
                chain.blocks[1].hash.substring(0, miner.chain.difficulty)
        )

        miner.stop()


    }


//    @Test
//    fun `Generate beautiful chain`(){
//        val chain = BaseBlockChainProducer(2)
//        for(i in 0.until(100)){
//            chain.pushBlock(chain.createBlock(listOf(), BaseJSONSerializer()))
//        }
//        println(BaseJSONSerializer().serialize(chain.produce(BaseJSONSerializer())))
//    }

}