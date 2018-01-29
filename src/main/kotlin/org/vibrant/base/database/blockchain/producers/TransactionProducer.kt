package org.vibrant.base.database.blockchain.producers

import org.vibrant.base.database.blockchain.models.TransactionModel
import org.vibrant.core.ModelProducer

/**
 * Producer class for abstract transaction
 */
abstract class TransactionProducer<out T: TransactionModel>: ModelProducer<T>()