package org.vibrant.core.producers

import org.vibrant.core.ModelProducer
import org.vibrant.core.models.Model

abstract class BlockProducer<out T : Model>: ModelProducer<T>()