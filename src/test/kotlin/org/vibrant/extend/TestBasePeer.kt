package org.vibrant.extend

import kotlinx.coroutines.experimental.async
import org.junit.Assert.assertEquals
import org.junit.Test
import org.vibrant.core.base.BaseJSONSerializer
import org.vibrant.core.base.models.BaseBlockChainModel
import org.vibrant.core.base.models.BaseMessageModel
import org.vibrant.core.base.models.BaseTransactionModel
import org.vibrant.core.base.node.BaseJSONRPCProtocol
import org.vibrant.core.base.node.BaseMiner
import org.vibrant.core.base.node.BaseNode
import org.vibrant.core.base.node.BasePeer
import org.vibrant.core.base.producers.BaseBlockChainProducer
import org.vibrant.core.base.producers.BaseTransactionProducer
import org.vibrant.core.node.RemoteNode
import org.vibrant.core.reducers.SignatureProducer
import org.vibrant.core.util.AccountUtils
import org.vibrant.core.util.HashUtils
import java.security.KeyPair
import java.util.*

class TestBasePeer {


    @Test
    fun `Test peer echo`(){
        val node1 = BaseNode(7000)
        val node2 = BaseNode(7001)


        node1.start()
        node2.start()

        val some = node1.connect(RemoteNode("localhost", 7001))
        // check ping - pong
        assertEquals(
                true,
                some
        )

        // connection established

        assertEquals(
                node1.peer.peers.size,
                1
        )

        assertEquals(
                node2.peer.peers.size,
                1
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


        Thread.sleep(1000)
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