package org.vibrant.core.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.vibrant.core.account.Account
import org.vibrant.core.entity.transaction.Transaction
import org.vibrant.core.serialization.EntitySerializer
import org.vibrant.core.util.AccountUtils
import org.vibrant.core.util.HashUtils


abstract class SerializableEntity