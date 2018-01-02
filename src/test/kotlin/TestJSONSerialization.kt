import org.junit.Assert.assertEquals
import org.junit.Test
import org.vibrant.core.account.Account
import org.vibrant.core.account.PublicAccount
import org.vibrant.core.entity.transaction.Transaction
import org.vibrant.core.entity.transaction.contract.ContractManifest
import org.vibrant.core.entity.transaction.contract.DeployContract
import org.vibrant.core.entity.transaction.transfer.Transfer
import org.vibrant.core.serialization.JSONSerializer
import org.vibrant.core.util.AccountUtils
import org.vibrant.core.util.HashUtils


class TestJSONSerialization {


    @Test
    fun `JSON entity serialization`(){
        val sender = Account(AccountUtils.generateKeyPair())
        val receiver = PublicAccount("user2")
        val serializer = JSONSerializer(false)
        val transaction = Transaction(Transfer(receiver.publicKey, 10), sender)
        val data = serializer.serializeEntity(transaction)
        val expectedSignature = HashUtils.bytesToHex(AccountUtils.signData(data, sender.keyPair))
        transaction.sign(serializer)
        assertEquals(
                "{\"content\":{\"receiver\":\"user2\",\"amount\":10},\"signature\":\"$expectedSignature\"}",
                transaction.serialize(serializer)
        )

    }


    @Test
    fun `JSON smart contract serialization`(){
        val sender = Account(AccountUtils.generateKeyPair())
        val serializer = JSONSerializer(false)
        val transaction = Transaction(
                DeployContract(
                        ContractManifest(
                                ContractManifest.LANG.TEST, -1),
                        "some compiled code"),
                sender)
        val data = serializer.serializeEntity(transaction)
        val expectedSignature = HashUtils.bytesToHex(AccountUtils.signData(data, sender.keyPair))
        transaction.sign(serializer)
        assertEquals(
                "{\"content\":{\"manifest\":{\"lang\":\"TEST\",\"version\":-1},\"bytecode\":\"some compiled code\"},\"signature\":\"$expectedSignature\"}",
                transaction.serialize(serializer)
        )
    }


    @Test
    fun `Deserialization works with types`(){
//        val seri = "{\"content\":{\"manifest\":{\"lang\":\"TEST\",\"version\":-1},\"bytecode\":\"some compiled code\"},\"signature\":\"signature_here_hahah\"}"
//        val some = JSONSerializer().deserializeEntity(seri)
//        println(some)
    }
}