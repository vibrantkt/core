package org.vibrant.base.database.blockchain


import org.vibrant.base.database.blockchain.models.BlockChainModel
import org.vibrant.base.database.blockchain.models.BlockModel

/**
 * @property blocks Ordered list of blocks from genesis to the last
 */
abstract class InMemoryBlockChain<B: BlockModel, T: BlockChainModel>: BlockChain<B, T>() {

    protected val blocks = arrayListOf(this.createGenesisBlock())

    override fun latestBlock(): B = this.blocks.last()

    override fun addBlock(block: B): B {
        synchronized(this.blocks, {
            this.blocks.add(block)
            this.notifyNewBlock()
            return this.latestBlock()
        })
    }
}