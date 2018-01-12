package org.vibrant.core.producers

import org.vibrant.core.ModelProducer
import org.vibrant.core.models.TransactionModel

abstract class TransactionProducer<T: TransactionModel>: ModelProducer<T>()