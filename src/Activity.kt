import de.th_koeln.basicstage.Actor
import de.th_koeln.basicstage.coordinatesystem.WorldConstants
import de.th_koeln.imageprovider.Assets
import kotlin.math.absoluteValue

class Activity(val description: String, private val energyImpact: Int, private val happinessImpact: Int, val onButtonClick: (activity: Activity) -> Unit) {
    val button: Actor = Actor(Assets.EMPTY, widthInit = 160, heightInit = 40)

    init {
        button.text.textBackground = Assets.textBackgrounds.BLUE_BUTTON
        button.text.content = description

        button.reactionForMouseClick = {
            onButtonClick(this)
        }
    }

    fun execute(pet: Pet): Pet {
        val curHappiness = pet.happiness
        val curHealth = pet.getHealth()

        if (curHealth.energy < energyImpact.absoluteValue) {
            println("Energy level to low for Activity!")
            return pet
        }

        val newHealth = Health(curHealth.energy + energyImpact, curHealth.fitness)

        return pet.copy(health = newHealth, happiness = curHappiness + happinessImpact)
    }
}