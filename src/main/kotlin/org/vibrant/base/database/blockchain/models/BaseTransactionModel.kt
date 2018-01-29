@file:Suppress("ArrayInDataClass")

package org.vibrant.base.database.blockchain.models

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName

/***
 * Model of transaction which is serializable
 * @property from address of sender
 * @property to address of receiver
 * @property payload any content
 * @property signature signature
 */
abstract class BaseTransactionModel(
        val from: String,
        val to: String,
        val payload: TransactionPayload,
        val signature: String
): TransactionModel()