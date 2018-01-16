package org.vibrant.core

import org.vibrant.core.models.Model

abstract class ConcreteModelSerializer<out T: Model>: ModelSerializer() {
    abstract override fun deserialize(serialized: ByteArray): T
}