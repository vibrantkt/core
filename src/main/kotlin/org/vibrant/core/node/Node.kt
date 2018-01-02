package org.vibrant.core.node

import org.vibrant.core.chain.BlockChain

abstract class Node {
    private val chain = BlockChain()
    abstract fun save()
    abstract fun load()
}