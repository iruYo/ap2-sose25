import de.th_koeln.basicstage.Actor
import de.th_koeln.imageprovider.Assets
import kotlin.math.absoluteValue

open class Activity(
    val description: String,
    private val energyImpact: Int,
    private val happinessImpact: Int,
    private val onButtonClick: (activity: Activity) -> Unit,
    private val stageEffect: () -> Unit = {}
) {
    val button: Actor = Actor(Assets.EMPTY, widthInit = 160, heightInit = 40)

    init {
        button.text.textBackground = Assets.textBackgrounds.BLUE_BUTTON
        button.text.content = description

        button.reactionForMouseClick = {
            onButtonClick(this)
        }
    }

    open fun execute(pet: Pet): Pet {
        val curHealth = pet.health

        if (curHealth.energy < energyImpact.absoluteValue) {
            throw IllegalStateException("Energy level too low for activity!")
        }

        var calculatedHappiness = happinessImpact
        if (pet.lastActivity == this.description) {
            calculatedHappiness -= (10..30).random()
        }

        stageEffect.invoke()

        return pet.clone(
            initHealth = Health(curHealth.energy + energyImpact, curHealth.fitness),
            initHappiness = pet.happiness + calculatedHappiness,
            initLastActivity = this.description
        )
    }
}

data class BakeCookies(
    private val onButtonClick: (activity: Activity) -> Unit,
    private val stageEffect: () -> Unit = {}
) : Activity("Bake Cookies", -5, 10, onButtonClick, stageEffect) {
    override fun execute(pet: Pet): Pet {
        return super.execute(pet.handleItem(Item("COOKIE", ItemCategory.FOOD, 1)))
    }
}

data class PlayBall(
    private val onButtonClick: (activity: Activity) -> Unit,
    private val stageEffect: () -> Unit = {}
) : Activity("Play Ball", -15, 30, onButtonClick, stageEffect) {
    private val requirements: List<String> = listOf("BALL")

    override fun execute(pet: Pet): Pet {
        if (!requirements.all { pet.hasItem(it) }) {
            throw IllegalStateException("Pet is missing required items for playing Ball!")
        }

        return super.execute(pet)
    }
}

data class Run(
    private val onButtonClick: (activity: Activity) -> Unit,
    private val stageEffect: () -> Unit = {}
) : Activity("Run", -10, 20, onButtonClick, stageEffect) {
    override fun execute(pet: Pet): Pet {
        println("Puh!")
        return super.execute(pet)
    }
}