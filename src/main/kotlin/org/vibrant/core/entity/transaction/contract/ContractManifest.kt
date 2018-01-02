package org.vibrant.core.entity.transaction.contract

import org.vibrant.core.entity.SerializableEntity

class ContractManifest(val lang: LANG, val version: Int): SerializableEntity() {

    enum class LANG{
        TEST
    }
}