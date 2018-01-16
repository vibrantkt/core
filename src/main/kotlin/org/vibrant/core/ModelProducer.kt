package org.vibrant.core

import org.vibrant.core.models.Model

abstract class ModelProducer<out B: Model> {
    abstract fun produce(serializer: ModelSerializer): B
}