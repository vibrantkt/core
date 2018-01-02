package org.vibrant.core.entity.transaction.contract

import org.vibrant.core.entity.transaction.TransactionContent

class DeployContract(val manifest: ContractManifest, val bytecode: String): TransactionContent()