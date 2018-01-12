package org.vibrant.core

import org.vibrant.core.models.Model

abstract class ModelSerializer {
    abstract fun serialize(model: Model): String
    abstract fun deserialize(serialized: String): Model
}