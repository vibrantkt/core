package org.vibrant.core.node

import org.vibrant.core.producers.BlockChainProducer

abstract class Node {
    abstract fun save()
    abstract fun load()
}