package org.vibrant.core

import org.vibrant.core.models.Model

abstract class ModelProducer<T : Model> {
    abstract fun produce(serializer: ModelSerializer<T>): T
}