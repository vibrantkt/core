package org.vibrant.base.rpc.json

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class SimpleJSONRPCError(
        @JsonSerialize(using = ErrorCodeSerializer::class) val code: ERROR_CODE,
        val message: String,
        val data: String){



    enum class ERROR_CODE(val value: Int){
        METHOD_NOT_FOUND(-32601),
        PARSE_ERROR(-32700),
        INVALID_REQUEST(-32600),
        INVALID_PARAMETERS(-32602),
        INTERNAL_ERROR(-32603);

        companion object {
            @JsonCreator
            @JvmStatic
            fun fromErrorCode(code: String): ERROR_CODE{
                return values().firstOrNull { it.value == code.toInt()}!!
            }
        }

    }
}
