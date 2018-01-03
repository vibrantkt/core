@file:Suppress("ArrayInDataClass")

package org.vibrant.core.base

import org.vibrant.core.models.TransactionModel

/***
 * Model of transaction which is serializable
 * @property from address of sender
 * @property to address of receiver
 * @property payload any content
 * @property signature signature
 */
data class BaseTransactionModel(
        val from: ByteArray,
        val to: ByteArray,
        val payload: ByteArray,
        val signature: ByteArray
): TransactionModel()