package org.vibrant.core

import org.vibrant.core.models.Model

abstract class ModelSerializer {
    abstract fun serialize(model: Model): ByteArray
    abstract fun deserialize(serialized: ByteArray): Model
}