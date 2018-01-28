package org.vibrant.base.rpc.json

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.vibrant.core.ConcreteModelSerializer
import org.vibrant.core.models.Model
import java.util.HashMap

object JSONRPCSerializer: ConcreteModelSerializer<JSONRPCEntity>() {

    override fun serialize(model: Model): ByteArray {
        return jacksonObjectMapper().writeValueAsBytes(model)
    }

    override fun deserialize(serialized: ByteArray): JSONRPCEntity {
        val map: HashMap<String, Any> = jacksonObjectMapper().readValue(serialized, object : TypeReference<Map<String, Any>>(){})
        return if(map.containsKey("method")){
            jacksonObjectMapper().readValue(serialized, JSONRPCRequest::class.java)
        }else if(map.containsKey("result") || map.containsKey("error")){
            jacksonObjectMapper().readValue(serialized, JSONRPCResponse::class.java)
        }else{
            throw Exception("Unexpected json rpc entity")
        }
    }
}