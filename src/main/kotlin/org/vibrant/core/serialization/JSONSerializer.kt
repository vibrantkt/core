package org.vibrant.core.serialization

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.vibrant.core.entity.SerializableEntity


class JSONSerializer(val debug: Boolean = true) : EntitySerializer(){
    override fun deserializeEntity(serialized: String): SerializableEntity {
        return jacksonObjectMapper().readValue(serialized, SerializableEntity::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    override fun serializeEntity(entity: SerializableEntity): String {
        return if(debug)
            jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(entity)
        else
            jacksonObjectMapper().writeValueAsString(entity)
    }
}