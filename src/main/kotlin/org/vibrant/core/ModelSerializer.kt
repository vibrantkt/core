package org.vibrant.core

import org.vibrant.core.models.Model

abstract class ModelSerializer<T: Model> {
    abstract fun serialize(model: T): ByteArray
    abstract fun deserialize(serialized: ByteArray): T
}