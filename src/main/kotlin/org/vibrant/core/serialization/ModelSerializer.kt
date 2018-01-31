package org.vibrant.core.serialization

import org.vibrant.core.models.Model

abstract class ModelSerializer {
    abstract fun serialize(model: Model): ByteArray
    abstract fun deserialize(serialized: ByteArray): Model
}