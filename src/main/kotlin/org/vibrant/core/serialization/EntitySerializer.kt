package org.vibrant.core.serialization

import org.vibrant.core.entity.SerializableEntity

abstract class EntitySerializer {
    abstract fun serializeEntity(entity: SerializableEntity): String
    abstract fun deserializeEntity(serialized: String): SerializableEntity

}