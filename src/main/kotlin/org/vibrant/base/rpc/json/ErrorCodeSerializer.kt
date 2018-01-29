package org.vibrant.base.rpc.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.core.JsonProcessingException
import java.io.IOException
import com.fasterxml.jackson.databind.JsonSerializer


class ErrorCodeSerializer : JsonSerializer<SimpleJSONRPCError.ERROR_CODE>() {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(tmpInt: SimpleJSONRPCError.ERROR_CODE,
                           jsonGenerator: JsonGenerator,
                           serializerProvider: SerializerProvider) {
        jsonGenerator.writeNumber(tmpInt.value)
    }
}