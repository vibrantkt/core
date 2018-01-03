package org.vibrant.core.base

import org.vibrant.core.base.models.BaseBlockModel
import org.vibrant.core.models.TransactionModel
import org.vibrant.core.producers.BlockProducer


class BaseBlockProducer: BlockProducer<BaseBlockModel>(){
    val transactions: ArrayList<TransactionModel> = arrayListOf()


    override fun produce(): BaseBlockModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}