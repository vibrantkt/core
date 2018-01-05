package org.vibrant.core.base

import com.fasterxml.jackson.annotation.JsonTypeName
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.vibrant.core.base.models.BaseBlockChainModel
import org.vibrant.core.base.models.BaseBlockModel
import org.vibrant.core.base.models.BaseTransactionModel
import org.vibrant.core.models.BlockChainModel
import org.vibrant.core.models.Model
import org.vibrant.core.reducers.ModelSerializer
import java.util.HashMap




class BaseJSONSerializer : ModelSerializer(){
    override fun deserialize(serialized: String): Model {
        val map: HashMap<String, Any> = jacksonObjectMapper().readValue(serialized, object : TypeReference<Map<String, Object>>(){})

        val targetType = when(map["@type"]){
            BaseTransactionModel::class.java.getAnnotation(JsonTypeName::class.java).value -> {
                BaseTransactionModel::class.java
            }
            BaseBlockModel::class.java.getAnnotation(JsonTypeName::class.java).value -> {
                BaseBlockModel::class.java
            }
            BaseBlockChainModel::class.java.getAnnotation(JsonTypeName::class.java).value -> {
                BaseBlockChainModel::class.java
            }
            else -> {
                throw Exception("Can't deserialize type ${map["@type"]}")
            }
        }

        return jacksonObjectMapper().readValue(serialized, targetType)
    }

    override fun <T : Model> serialize(model: T): String {
        return jacksonObjectMapper().writeValueAsString(model)
    }

}