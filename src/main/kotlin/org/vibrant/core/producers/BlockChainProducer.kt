package org.vibrant.core.producers

import org.vibrant.core.ModelProducer
import org.vibrant.core.models.BlockChainModel

abstract class BlockChainProducer<out T: BlockChainModel>: ModelProducer<T>()