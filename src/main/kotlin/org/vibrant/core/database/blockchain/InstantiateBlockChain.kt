package org.vibrant.core.database.blockchain

import org.vibrant.core.models.blockchain.BlockChainModel
import org.vibrant.core.models.block.BlockModel

interface InstantiateBlockChain<B: BlockModel, T: BlockChainModel> {
    fun asBlockChainProducer(model: T): BlockChain<B, T>
}