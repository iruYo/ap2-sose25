import de.th_koeln.basicstage.Actor
import de.th_koeln.imageprovider.Assets
import kotlin.math.absoluteValue

data class Activity(
    val description: String,
    private val energyImpact: Int,
    private val happinessImpact: Int,
    private val onButtonClick: (activity: Activity) -> Unit,
    private val stageEffect: () -> Unit = {},
    private val requirements: List<String> = emptyList()
) {
    val button: Actor = Actor(Assets.EMPTY, widthInit = 160, heightInit = 40)

    init {
        button.text.textBackground = Assets.textBackgrounds.BLUE_BUTTON
        button.text.content = description

        button.reactionForMouseClick = {
            onButtonClick(this)
        }
    }

    fun execute(pet: Pet): Pet {
        val curHealth = pet.health

        if (curHealth.energy < energyImpact.absoluteValue) {
            throw IllegalStateException("Energy level too low for activity!")
        }

        val petHasRequirements = requirements.all { pet.hasItem(it) }

        if (!petHasRequirements) {
            throw IllegalStateException("Pet is missing required items for activity!")
        }

        var calculatedHappiness = happinessImpact
        if (pet.lastActivity == this.description) {
            calculatedHappiness -= (10..30).random()
        }

        stageEffect.invoke()

        return pet.clone(initHealth = Health(curHealth.energy + energyImpact, curHealth.fitness), initHappiness = pet.happiness + calculatedHappiness, initLastActivity = this.description)
    }
}