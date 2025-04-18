import de.th_koeln.basicstage.Actor
import de.th_koeln.imageprovider.Assets

data class Pet(val name: String, private val health: Health, val happiness: Int) {
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

        private fun checkForHunger(curEnergy: Int): Boolean {
            return curEnergy < 20
        }

        private fun setZeroIfNegative(cur: Int, impact: Int): Int {
            var newValue = cur + impact

            if (newValue < 0) {
                newValue = 0
            }

            return newValue
        }
    }
    private var hungry: Boolean
    private var _health = health
    private var _happiness = happiness
    private var inventory: List<Item> = emptyList()

    private val entity: Actor = Actor(Assets.dog.HAPPY, 0, 300)
    private val energyBar: Actor = Actor(Assets.EMPTY, 0, 0, 160, 40)
    private val hungerBar: Actor = Actor(Assets.EMPTY, 161, 0, 170, 40)
    private val happinessBar: Actor = Actor(Assets.EMPTY, 332, 0, 180, 40)

    init {
        energyBar.text.textBackground = Assets.textBackgrounds.STONE
        energyBar.text.content = energyBarText(_health.energy)

        happinessBar.text.textBackground = Assets.textBackgrounds.STONE
        happinessBar.text.content = happinessBarText(_happiness)

        hungry = checkForHunger(_health.energy)
        hungerBar.text.textBackground = Assets.textBackgrounds.STONE
        hungerBar.text.content = hungerBarText(this.hungry)

        entity.animation.everyNsteps.timeSpan = 40
        entity.animation.everyNsteps.reactionForTimePassed = {
            lifeGoesOn()
        }
    }

    fun doActivity(activity: Activity): Pet {
        println("Doing activity: ${activity.description}")
        return activity.execute(this)
    }

    fun handleItem(item: Item): Pet {
        if (item.category == ItemCategory.FOOD) {
            feed(item)
        } else {
            updateInventory { it.add(item) }
            use(item)
        }

        return this.copy(health = _health, happiness = _happiness)
    }

    fun removeItem(item: Item): Pet {
        updateInventory { it.remove(item) }
        updateHappiness(item.happinessImpact)
        return this
    }

    fun getHealth(): Health {
        return _health
    }

    fun getActors(): List<Actor> {
        return listOf(entity, energyBar, hungerBar, happinessBar)
    }

    private fun updateInventory(action: (MutableList<Item>) -> Unit) {
        val newInventory = inventory.toMutableList()

        action(newInventory)

        inventory = newInventory.toList()
    }

    private fun feed(item: Item) {
        updateEnergy(item.energyImpact)
    }

    private fun use(item: Item) {
        updateHappiness(item.happinessImpact)
    }

    private fun updateEnergy(energyImpact: Int) {
        _health = _health.copy(energy = setZeroIfNegative(_health.energy, energyImpact))

        hungry = checkForHunger(_health.energy)
        energyBar.text.content = energyBarText(_health.energy)
        hungerBar.text.content = hungerBarText(checkForHunger(_health.energy))
    }

    private fun updateHappiness(happinessImpact: Int) {
        _happiness += setZeroIfNegative(_happiness, happinessImpact)
        happinessBar.text.content = happinessBarText(_happiness)
    }

    private fun lifeGoesOn() {
        // 50 guaranteed to be random - https://xkcd.com/221/
        if (50 <= (0..100).random()) {
            updateEnergy(-10)
            println("Life went on!")
        }
    }
}