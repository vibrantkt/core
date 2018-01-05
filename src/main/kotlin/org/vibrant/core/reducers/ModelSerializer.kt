package org.vibrant.core.reducers

import org.vibrant.core.models.Model

abstract class ModelSerializer {
    abstract fun <T: Model>serialize(model: T): String
    abstract fun deserialize(serialized: String): Model
}