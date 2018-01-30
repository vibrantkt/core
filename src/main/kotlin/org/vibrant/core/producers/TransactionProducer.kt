package org.vibrant.core.producers

import org.vibrant.core.models.transaction.TransactionModel
import org.vibrant.core.ModelProducer

/**
 * Producer class for abstract transaction
 */
abstract class TransactionProducer<out T: TransactionModel>: ModelProducer<T>()