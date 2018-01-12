package org.vibrant.core.producers

import org.vibrant.core.ModelProducer
import org.vibrant.core.models.BlockChainModel
import org.vibrant.core.models.BlockModel

abstract class BlockChainProducer<out T: BlockChainModel>: ModelProducer<T>(){
    private val onNewBlockListener = arrayListOf<NewBlockListener>()



    fun addNewBlockListener(listener: NewBlockListener) = this.onNewBlockListener.add(listener)
    fun removeNewBlockListener(listener: NewBlockListener) = this.onNewBlockListener.remove(listener)


    abstract class NewBlockListener{
        abstract fun nextBlock(blockModel: BlockModel)
    }
}