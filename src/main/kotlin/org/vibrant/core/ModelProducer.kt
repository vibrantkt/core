package org.vibrant.core

import org.vibrant.core.models.Model

abstract class ModelProducer<out T : Model> {
    abstract fun produce(serializer: ModelSerializer): T
}