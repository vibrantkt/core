package org.vibrant.base.smartvm

abstract class SmartContractVM<in T: SmartContractPayload, in B: SmartContract> {
    abstract fun transaction(from: String, to: String, payload: T)
    abstract fun deploy(contract: B)
}