import com.rakaneth.GameConfig
import com.rakaneth.engine.GameElement
import com.rakaneth.engine.GameState
import com.rakaneth.engine.Messenger
import com.rakaneth.engine.effect.InstantDamageEffect
import org.junit.jupiter.api.*
import org.slf4j.LoggerFactory
import java.util.logging.LogManager

class EffectTest {
    private val logger = LoggerFactory.getLogger(EffectTest::class.java)

    companion object {
        @BeforeAll
        @JvmStatic
        fun gameState() {
            GameConfig().newGame()
            val stream = EffectTest::class.java.classLoader.getResourceAsStream("logging.properties")
            stream?.use {
                LogManager.getLogManager().readConfiguration(it)
            }
        }


    }

    @AfterEach
    fun displayMessages() {
        Messenger.messages.forEach { logger.info(it)}
        Messenger.messages.clear()
    }

    @Test
    @DisplayName("Should return the correct damage string")
    fun whenApplyDamage_shouldReturnCorrectString() {
        val dmgEffect = InstantDamageEffect(5, GameElement.Fire)
        dmgEffect.apply(GameState.player)

        //do NOT do this in game code - NOTHING should do NONE damage
        val weakEffect = InstantDamageEffect(5, GameElement.None)
        weakEffect.apply(GameState.player)
    }
}