package org.vibrant.core.serialization

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.vibrant.core.models.Model


class JSONSerializer(val debug: Boolean = true) : EntitySerializer(){

    override fun deserializeEntity(serialized: String): Model {
        return jacksonObjectMapper().readValue(serialized, Model::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    override fun serializeEntity(entity: Model): String {
        return if(debug)
            jacksonObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(entity)
        else
            jacksonObjectMapper().writeValueAsString(entity)
    }
}