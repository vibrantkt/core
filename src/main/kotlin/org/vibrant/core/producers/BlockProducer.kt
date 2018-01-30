package org.vibrant.core.producers

import org.vibrant.core.models.block.BlockModel
import org.vibrant.core.ModelProducer

abstract class BlockProducer<out T: BlockModel>: ModelProducer<T>()