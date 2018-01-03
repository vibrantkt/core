package org.vibrant.core.serialization

import org.vibrant.core.models.Model

abstract class EntitySerializer {
    abstract fun serializeEntity(entity: Model): String
    abstract fun deserializeEntity(serialized: String): Model

}