package org.vibrant.core.producers

import org.vibrant.core.models.block.BlockModel

abstract class BlockProducer<out T: BlockModel>: ModelProducer<T>()