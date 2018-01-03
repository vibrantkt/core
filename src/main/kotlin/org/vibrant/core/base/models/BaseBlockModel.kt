package org.vibrant.core.base.models

import org.vibrant.core.models.BlockModel

class BaseBlockModel(
        hash: String,
        prevHash: String
): BlockModel(
        hash.toByteArray(),
        prevHash.toByteArray()
){

}