import de.th_koeln.basicstage.Actor
import de.th_koeln.basicstage.Stage

class Game(private val stage: Stage) {
    companion object {
    }

    private var pet: Pet = Pet("Guy", Health(10, 50), 0)
    private val snackCount: Int = 5
    private val toysCount: Int = 2
    private val buttonHeight = 40

    private val snacks: List<Item>
    private val toys: List<Item>
    private val activities: List<Activity>

    private fun updatePet(action: () -> Pet) {
        val newPet = action()
        if (pet != newPet) {
            removePetFromStage()
            pet = newPet
            addPetToStage()
        }
    }

    private val petDoesActivity: () -> (activity: Activity) -> Unit = {
        { activity ->
            updatePet { pet.doActivity(activity) }
        }
    }

    init {
        snacks = snackCount.generateItems(ItemCategory.FOOD)
        toys = toysCount.generateItems(ItemCategory.TOY)
        activities = listOf(
            Activity("Bake Cookies", -5, 10, petDoesActivity()),
            Activity("Play Ball", -20, 30, petDoesActivity()),
            Activity("Run", -15, 20, petDoesActivity())
        )

        addPetToStage()

        (activities.asSequence() zip generateSequence(buttonHeight) { it + buttonHeight })
            .map { (activity, yInit) -> activity.button.y = yInit; activity.button }
            .forEach { stage.addActor(it) }

        (snacks + toys)
            .map { it.actors }
            .flatten()
            .forEach { stage.addActor(it) }
    }

    private fun addPetToStage() {
        pet.getActors().forEach { stage.addActor(it) }
    }

    private fun removePetFromStage() {
        pet.getActors().forEach { stage.removeActor(it) }
    }

    private fun Int.generateItems(category: ItemCategory): List<Item> {
        val addToInventoryAndRemoveFromStage: () -> (item: Item, actor: Actor) -> Unit = {
            { item, actor ->
                stage.removeActor(actor)

                updatePet { pet.handleItem(item) }
            }
        }

        return List(this) {
            Item(
                Item.assetsByCategoryMapAndName[category]!!.keys.random(),
                category,
                1,
                addToInventoryAndRemoveFromStage()
            )
        }
    }
}

