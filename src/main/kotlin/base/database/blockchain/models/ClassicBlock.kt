package org.vibrant.base.database.blockchain.models

abstract class ClassicBlock(open val index: Long, open val hash: String, open val previousHash: String): BlockModel()