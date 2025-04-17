import de.th_koeln.basicstage.Actor
import de.th_koeln.imageprovider.Assets

class Pet(val name: String, private var health: Health = Health(40, 50), private var happiness: Int = 0) {
    companion object {
        private const val ENERGYBAR_CONST = "ENERGY: "
        private const val HAPPINESSBAR_CONST = "HAPPINESS: "
        private const val HUNGERBAR_CONST = "HUNGRY: "

        private fun hungerBarText(text: Any): String {
            return "$HUNGERBAR_CONST $text"
        }

        private fun energyBarText(text: Any): String {
            return "$ENERGYBAR_CONST $text"
        }

        private fun happinessBarText(text: Any): String {
            return "$HAPPINESSBAR_CONST $text"
        }
    }

    val entity: Actor = Actor(Assets.dog.HAPPY, 0, 300)
    val energyBar: Actor = Actor(Assets.EMPTY, 0, 0, 160, 40)
    val hungerBar: Actor = Actor(Assets.EMPTY, 161, 0, 170, 40)
    val happinessBar: Actor = Actor(Assets.EMPTY, 332, 0, 180, 40)
    private var hungry: Boolean = checkForHunger()

    private var inventory: MutableList<Item> = emptyList<Item>().toMutableList() //?!

    init {
        energyBar.text.textBackground = Assets.textBackgrounds.STONE
        energyBar.text.content = energyBarText(health.energy)

        happinessBar.text.textBackground = Assets.textBackgrounds.STONE
        happinessBar.text.content = happinessBarText(happiness)

        hungerBar.text.textBackground = Assets.textBackgrounds.STONE
        hungerBar.text.content = hungerBarText(this.hungry)

        entity.animation.everyNsteps.timeSpan = 40
        entity.animation.everyNsteps.reactionForTimePassed = {
            lifeGoesOn()
        }
    }

    fun handleItem(item: Item): Pet {
        if (item.category == ItemCategory.FOOD) {
            feed(item)
        } else {
            inventory.add(item)
            consooom(item)
        }

        return this
    }

    fun removeItem(item: Item): Pet {
        inventory.remove(item)
        updateHappiness(this.happiness - item.happinessImpact)
        return this
    }

    private fun feed(item: Item) {
        updateEnergy(this.health.energy + item.energyImpact)
    }

    private fun consooom(item: Item) {
        updateHappiness(this.happiness + item.happinessImpact)
    }

    private fun checkForHunger(): Boolean {
        return health.energy < 20
    }

    private fun updateEnergy(newEnergy: Int) {
        this.health.energy = newEnergy
        this.hungry = checkForHunger()

        energyBar.text.content = energyBarText(this.health.energy)
        hungerBar.text.content = hungerBarText(this.hungry)
    }

    private fun updateHappiness(newHappiness: Int) {
        this.happiness = newHappiness

        happinessBar.text.content = happinessBarText(this.happiness)
    }

    private fun lifeGoesOn() {
        // 50 guaranteed to be random - https://xkcd.com/221/
        if (50 <= (0..100).random()) {
            updateEnergy(health.energy - 10)
        }
    }
}