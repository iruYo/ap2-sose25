import de.th_koeln.basicstage.Actor
import de.th_koeln.imageprovider.Assets

data class Pet(
    val name: String,
    private val initHealth: Health,
    private val initHappiness: Int,
    private val initEntity: Actor,
    private val initMinutesAwake: Int = 0,
    private val initLastActivity: String = "",
    private val initInventory: List<Item> = emptyList(),
) {
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
    private var _health: Health
    private var _happiness: Int
    private var _inventory: List<Item>
    private var _lastActivity: String

    private val hoursAwake: Float
        get() = minutesAwake / 60F
    private var minutesAwake: Int = initMinutesAwake

    private val entity: Actor = initEntity
    private val energyBar: Actor = Actor(Assets.EMPTY, 0, 0, 160, 40)
    private val hungerBar: Actor = Actor(Assets.EMPTY, 161, 0, 170, 40)
    private val happinessBar: Actor = Actor(Assets.EMPTY, 332, 0, 180, 40)

    val actors = mapOf("entity" to entity, "energyBar" to energyBar, "hungryBar" to hungerBar, "happinessBar" to happinessBar)

    val health: Health
        get() = _health

    val happiness: Int
        get() = _happiness

    val lastActivity
        get() = _lastActivity

    init {
        _health = initHealth
        _happiness = initHappiness
        _inventory = initInventory
        _lastActivity = initLastActivity

        energyBar.text.textBackground = Assets.textBackgrounds.STONE
        energyBar.text.content = energyBarText(_health.energy)

        happinessBar.text.textBackground = Assets.textBackgrounds.STONE
        happinessBar.text.content = happinessBarText(_happiness)

        hungry = checkForHunger(_health.energy)
        hungerBar.text.textBackground = Assets.textBackgrounds.STONE
        hungerBar.text.content = hungerBarText(this.hungry)

        entity.animation.everyNsteps.timeSpan = 40
        entity.animation.everyNsteps.reactionForTimePassed = {
            println("Minutes awake: $minutesAwake, Hours awake: $hoursAwake")

            lifeGoesOn()

            minutesAwake += 1
            if (minutesAwake % 10 == 0) {
                updateEnergy(-10)
            }
        }
    }

    fun doActivity(activity: Activity): Pet {
        println("Doing activity: ${activity.description}")

        return activity.execute(this)
    }

    fun handleItem(item: Item): Pet {
        println("Handling item: ${item.name}")

        if (item.category == ItemCategory.FOOD) {
            feed(item)
        } else {
            val inventoryItem = _inventory.find { it.name == item.name }
            if (inventoryItem == null) {
                updateInventory { it.add(item) }

                println("\tAdded ${item.name} to inventory!")
            } else {
                updateInventory { inventory ->
                    inventory.remove(inventoryItem)

                    val updatedItem = Item(inventoryItem.name, inventoryItem.category, inventoryItem.amount + 1)
                    inventory.add(updatedItem)

                    println("\tAmount of ${updatedItem.name} in inventory: ${updatedItem.amount}")
                }
            }
            use(item)
        }

        return clone()
    }

    fun deductedItemByName(itemName: String): Pet {
        val inventoryItem = _inventory.find { it.name == itemName }

        if (inventoryItem != null) {
            updateInventory { inventory -> inventory.remove(inventoryItem) }

            var newInventoryItemAmount = inventoryItem.amount
            if (newInventoryItemAmount > 1) {
                newInventoryItemAmount -= 1

                val newItem = Item(inventoryItem.name, inventoryItem.category, newInventoryItemAmount)
                updateInventory { inventory -> inventory.add(newItem) }
            }
        }

        return clone(initInventory = _inventory)
    }

    fun hasItem(itemName: String): Boolean {
        return _inventory.any { it.name == itemName }
    }

    fun addAnimation(animations: List<PropertyAnimation>): Pet {
        animations.forEach { entity.animation.queue.addPropertyAnimation(it) }

        return clone()
    }

    private fun updateInventory(action: (MutableList<Item>) -> Unit) {
        val newInventory = _inventory.toMutableList()

        action(newInventory)

        _inventory = newInventory.toList()
    }

    fun clone(initName: String = name, initHealth: Health = _health, initHappiness: Int = _happiness, initInventory: List<Item> = _inventory, initMinutesAwake: Int = minutesAwake, initLastActivity: String = _lastActivity, initEntity: Actor = entity): Pet {
        return Pet(name = initName, initHealth = initHealth, initHappiness = initHappiness, initInventory = initInventory, initMinutesAwake = initMinutesAwake, initLastActivity = initLastActivity, initEntity = initEntity)
    }

    private fun feed(item: Item) {
        if (item.category != ItemCategory.FOOD) {
            throw IllegalArgumentException("Can not eat item of type ${item.category}!")
        }
        updateEnergy(item.energyImpact)

        println("\tAte ${item.name}!")
    }

    private fun use(item: Item) {
        updateHappiness(item.happinessImpact)

        println("\tUsed ${item.name}!")
    }

    private fun updateEnergy(energyImpact: Int) {
        _health = _health.copy(energy = setZeroIfNegative(_health.energy, energyImpact))

        hungry = checkForHunger(_health.energy)
        energyBar.text.content = energyBarText(_health.energy)
        hungerBar.text.content = hungerBarText(checkForHunger(_health.energy))
    }

    private fun updateHappiness(happinessImpact: Int) {
        _happiness = setZeroIfNegative(_happiness, happinessImpact)
        happinessBar.text.content = happinessBarText(_happiness)
    }

    private fun lifeGoesOn() {
        // 30 guaranteed to be random - https://xkcd.com/221/
        if (30 <= (0..100).random()) {
            updateEnergy(-1)
        }
    }
}