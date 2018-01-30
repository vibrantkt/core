package org.vibrant.core.models.block

abstract class ClassicBlockModel(open val index: Long, open val hash: String, open val previousHash: String): BlockModel()