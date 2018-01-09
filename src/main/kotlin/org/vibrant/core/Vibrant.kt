package org.vibrant.core

import org.vibrant.core.models.BlockChainModel
import org.vibrant.core.node.AbstractNode
import org.vibrant.core.producers.BlockChainProducer

class Vibrant<A : BlockChainModel, B : BlockChainProducer<A>> {

    var node: AbstractNode<A, B>? = null

}