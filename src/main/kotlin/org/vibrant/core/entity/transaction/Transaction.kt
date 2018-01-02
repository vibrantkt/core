package org.vibrant.core.entity.transaction

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.vibrant.core.account.Account
import org.vibrant.core.entity.SerializableEntity
import org.vibrant.core.serialization.EntitySerializer
import org.vibrant.core.util.AccountUtils
import org.vibrant.core.util.HashUtils



@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
class Transaction(val content: TransactionContent, @JsonIgnore val author: Account): SerializableEntity(){
    var signature: String? = null
    private var signed = false

    fun serialize(serializer: EntitySerializer): String {
        // Serialized without signature
        if(!this.signed){
            throw Exception("Not signed yet")
        }else {
            return serializer.serializeEntity(this)
        }
    }

    fun sign(serializer: EntitySerializer){
        this.signature = null
        this.signature = HashUtils.bytesToHex(AccountUtils.signData(serializer.serializeEntity(this), author.keyPair))
        this.signed = true
    }
}