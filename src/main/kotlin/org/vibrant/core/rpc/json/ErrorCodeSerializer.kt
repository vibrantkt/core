package org.vibrant.core.rpc.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.core.JsonProcessingException
import java.io.IOException
import com.fasterxml.jackson.databind.JsonSerializer


class ErrorCodeSerializer : JsonSerializer<SimpleJSONRPCError.ErrorCode>() {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(tmpInt: SimpleJSONRPCError.ErrorCode,
                           jsonGenerator: JsonGenerator,
                           serializerProvider: SerializerProvider) {
        jsonGenerator.writeNumber(tmpInt.value)
    }
}