package org.vibrant.core

import org.vibrant.core.models.Model
import org.vibrant.core.reducers.ModelSerializer

abstract class ModelProducer<out T : Model> {
    abstract fun produce(serializer: ModelSerializer): T
}