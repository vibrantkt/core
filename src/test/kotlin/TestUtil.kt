import org.junit.Assert.assertEquals
import org.junit.Test
import org.vibrant.core.account.Account
import org.vibrant.core.util.AccountUtils
import org.vibrant.core.util.HashUtils

class TestUtil {



    @Test
    fun checkSignature(){
        val kp = AccountUtils.generateKeyPair()
        val some = AccountUtils.signData("Hello!", kp)
        Account(kp)
        assertEquals(
                true,
                AccountUtils.verifySignature("Hello!", kp.public, some)
        )
    }
}