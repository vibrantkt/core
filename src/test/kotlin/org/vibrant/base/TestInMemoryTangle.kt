package org.vibrant.base

import org.junit.Assert.*
import org.junit.Test
import org.vibrant.core.models.transaction.TransactionPayload
import org.vibrant.core.database.tangle.InMemoryTangle
import org.vibrant.core.database.tangle.TangleVertex
import org.vibrant.core.models.tangle.TangleModel
import org.vibrant.core.ModelSerializer
import org.vibrant.core.models.Model

class TestInMemoryTangle {

    data class Message(val content: String) : TransactionPayload()

    data class TestTangleModel(
            val children: List<Pair<String, String>>
    ): TangleModel()

    class TestTangle : InMemoryTangle<Message, TestTangleModel>(){
        override fun produce(serializer: ModelSerializer): TestTangleModel {
            return TestTangleModel(this.vertexes.values.flatMap { t ->
                t.parents.map { t.hash to it }
            })
        }

        override fun createGenesisVertex(): TangleVertex<Message> {
            return TangleVertex(listOf(), "0".repeat(10), Message("payload1"))
        }
    }

    private fun nominateTips(tangle: TestTangle): List<TangleVertex<Message>> {
        val tips = tangle.selectTips()
        return tips.subList(0, if(tips.size < 2) 1 else 2)
    }

    @Test
    fun `check tips selection and so on`(){
        val tangle = TestTangle()
        assertEquals(
                1,
                tangle.selectTips().size
        )

        var tips = nominateTips(tangle)

        tangle.addVertex(
                TangleVertex(
                        tips.map { it.hash },
                        "new tx",
                        Message("payload2")
                )
        )


        tips = nominateTips(tangle)

        assertEquals(
                2,
                tips.size
        )

        tangle.addVertex(
                TangleVertex(
                        tips.map { it.hash },
                        "new tx 2",
                        Message("payload3")
                )
        )

        tips = nominateTips(tangle)

        assertEquals(
                2,
                tips.size
        )

        val tModel = tangle.produce(object: ModelSerializer(){
            override fun serialize(model: Model): ByteArray {
                TODO("not implemented")
            }

            override fun deserialize(serialized: ByteArray): Model {
                TODO("not implemented")
            }
        })

        assertEquals(
                "\"new tx 2\" => \"0000000000\"\n\"new tx 2\" => \"new tx\"\n\"new tx\" => \"0000000000\"",
                tModel.children.joinToString("\n"){
                    "\"${it.first}\" => \"${it.second}\""
                }
        )

    }
}