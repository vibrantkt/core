package org.vibrant.extend

import org.junit.Assert.assertEquals
import org.junit.Test
import org.vibrant.core.producers.TransactionProducer
import org.vibrant.core.models.TransactionModel

class Extended{


    @Test
    fun `Extended transaction`(){
        val o = object: TransactionProducer<ExtendedTransactionModel>(){
            val from = "yura"
            val to = "vasya"
            val payload = "hello!"
            override fun produce(): ExtendedTransactionModel {
                return ExtendedTransactionModel(
                        from,
                        to,
                        payload
                )
            }
        }

        val etm = o.produce()
        assertEquals(
                "yura",
                etm.from
        )
        assertEquals(
                "vasya",
                etm.to
        )
        assertEquals(
                "hello!",
                etm.payload
        )

    }


    class ExtendedTransactionModel(val from: String, val to: String, val payload: String): TransactionModel()
}