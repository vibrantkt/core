package org.vibrant.core.producers

import org.vibrant.core.models.Model
import org.vibrant.core.serialization.ModelSerializer

abstract class ModelProducer<out B: Model> {
    abstract fun produce(serializer: ModelSerializer): B
}