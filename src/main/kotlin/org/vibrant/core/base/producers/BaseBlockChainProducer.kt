package org.vibrant.core.base.producers

import org.vibrant.core.ModelProducer
import org.vibrant.core.base.models.BaseBlockChainModel
import org.vibrant.core.base.models.BaseBlockModel
import org.vibrant.core.base.models.BaseTransactionModel
import org.vibrant.core.models.BlockModel
import org.vibrant.core.models.TransactionModel
import org.vibrant.core.producers.BlockChainProducer
import org.vibrant.core.reducers.ModelSerializer
import java.util.*

class BaseBlockChainProducer(val difficulty: Int = 1) : BlockChainProducer<BaseBlockChainModel>(){

    val blocks = arrayListOf(
            this.createGenesisBlock()
    )



    override fun produce(serializer: ModelSerializer): BaseBlockChainModel {
        return BaseBlockChainModel(
                blocks
        )
    }


    fun latestBlock(): BaseBlockModel {
        return this.blocks.last()
    }

    fun createBlock(transactions: List<BaseTransactionModel>, serializer: ModelSerializer, startNonce: Long = 0, timestamp: Long = Date().time): BaseBlockModel {
        return BaseBlockProducer(
                this.latestBlock().index + 1,
                this.latestBlock().hash,
                timestamp,
                transactions,
                startNonce,
                this.difficulty
        ).produce(serializer)
    }


    fun pushBlock(block: BaseBlockModel): BaseBlockModel {
        this.blocks.add(block)
        return this.latestBlock()
    }


    fun checkIntegrity(): Boolean{
        this.blocks.reduce({a,b ->
            if(a.hash == b.prevHash){
                return@reduce b
            }else{
                return false
            }
        })
        return true
    }

    private fun createGenesisBlock(): BaseBlockModel {
        return BaseBlockModel(
                0,
                "Genesis block hash",
                "",
                0,
                listOf(),
                0
        )
    }



    fun dump(blockChainModel: BaseBlockChainModel){
        this.blocks.clear()
        this.blocks.addAll(blockChainModel.blocks)
    }

    companion object {

        fun instantiate(blockChainModel: BaseBlockChainModel): BaseBlockChainProducer {
            val producer = BaseBlockChainProducer()
            producer.blocks.clear()
            producer.blocks.addAll(blockChainModel.blocks)
            return producer
        }
    }

}