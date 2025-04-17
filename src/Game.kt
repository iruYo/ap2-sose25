import de.th_koeln.basicstage.Actor
import de.th_koeln.basicstage.Stage

class Game(private val stage: Stage) {
    private val pet = Pet("Guy")
    private val snackCount: Int = 5
    private val toysCount: Int = 2
    private val snacks: List<Item>
    private val toys: List<Item>

    init {
        snacks = (snackCount.generateItems(ItemCategory.FOOD))
        toys = (toysCount.generateItems(ItemCategory.TOY))

        (snacks + toys)
            .map {   it.actors }
            .flatten()
            .forEach { stage.addActor(it) }

        stage.addActor(pet.entity)
        stage.addActor(pet.energyBar)
        stage.addActor(pet.hungerBar)
        stage.addActor(pet.happinessBar)
    }

    private fun Int.generateItems(category: ItemCategory): List<Item> {
        val addToInventoryAndRemoveFromStage: (item: Item) -> (actor: Actor) -> Unit = {
            item -> { actor ->
                pet.handleItem(item)
                stage.removeActor(actor)
            }
        }

        return generateSequence{ Item(Item.assetsByCategoryMapAndName[category]!!.keys.random(), category, 1) }
            .map {
                it.onActorClick = addToInventoryAndRemoveFromStage(it)
                it
            }
            .take(this)
            .toList()
    }
}

