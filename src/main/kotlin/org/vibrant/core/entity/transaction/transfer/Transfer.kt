package org.vibrant.core.entity.transaction.transfer

import org.vibrant.core.entity.transaction.TransactionContent

class Transfer(val receiver: String, val amount: Long): TransactionContent()