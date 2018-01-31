package org.vibrant.core.producers

import org.vibrant.core.models.transaction.TransactionModel

/**
 * Producer class for abstract transaction
 */
abstract class TransactionProducer<out T: TransactionModel>: ModelProducer<T>()