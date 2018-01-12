package org.vibrant.core

import org.vibrant.core.models.Model

abstract class ModelSerializer<T: Model> {
    abstract fun <B: T>serialize(model: B): String
    abstract fun deserialize(serialized: String): T
}