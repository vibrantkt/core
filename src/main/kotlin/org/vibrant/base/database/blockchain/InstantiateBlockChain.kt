package org.vibrant.base.database.blockchain

import org.vibrant.base.database.blockchain.models.BlockChainModel
import org.vibrant.base.database.blockchain.models.BlockModel

interface InstantiateBlockChain<B: BlockModel, T: BlockChainModel> {
    fun asBlockChainProducer(model: T): BlockChain<B, T>
}