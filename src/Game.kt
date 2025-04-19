import de.th_koeln.basicstage.Actor
import de.th_koeln.basicstage.Stage
import de.th_koeln.basicstage.coordinatesystem.WorldConstants
import de.th_koeln.imageprovider.Assets

class Game(private val stage: Stage) {
    private var pet: Pet = Pet("Guy", Health(100, 50), 0,
        Actor(Assets.dog.HAPPY, xInit = WorldConstants.STAGE_WIDTH/2, yInit = WorldConstants.STAGE_HEIGHT/2))
    private val buttonHeight: Int = 40

    private val snacks: List<Actor>
    private val toys: List<Actor>
    private val activities: List<Activity>

    private val petDoesActivity: () -> (activity: Activity) -> Unit = {
        { activity ->
            updatePet { pet.doActivity(activity) }
        }
    }

    private val spawnCookies: () -> Unit = {
        repeat((2..4).random()) {
            stage.addActor(generateItemActor("COOKIE"))
        }
    }

    private val petRunsLeftToRight: () -> Unit = {
        val curEntity = pet.actors["entity"]!!

        updatePet {
            val start = curEntity.x
            val end = curEntity.x
            val movement = start - 100 // move to the left
            pet.addAnimation(
                listOf(
                    PropertyAnimationValueChange(
                        _start = start,
                        _end = movement,
                        numberOfSteps = 20,
                        actor = curEntity,
                        propertyName = AnimatableProperties.X
                    ),
                    PropertyAnimationValueChange(
                        _start = movement,
                        _end = end,
                        numberOfSteps = 20,
                        actor = curEntity,
                        propertyName = AnimatableProperties.X
                    )
                )
            )
        }
    }

    private val playBall: () -> Unit = {
        updatePet { pet.deductedItemByName("BALL") }
        stage.addActor(generateItemActor("BALL"))
    }

    init {
        val snackCount = 7
        val toysCount = 20

        snacks = List(snackCount) { generateCategoryItem(ItemCategory.FOOD) }
        toys = List(toysCount) { generateCategoryItem(ItemCategory.TOY) }

        activities = listOf(
            Activity("Bake Cookies", -5, 10, petDoesActivity(), stageEffect = spawnCookies),
            Activity("Play Ball", -20, 30, petDoesActivity(), requirements = listOf("BALL"), stageEffect = playBall),
            Activity("Run", -15, 20, petDoesActivity(), stageEffect = petRunsLeftToRight),
        )

        stage.addActor(pet.actors["entity"]!!)
        addPetToStage()

        (activities.asSequence() zip generateSequence(buttonHeight) { it + buttonHeight })
            .map { (activity, yInit) -> activity.button.y = yInit; activity.button }
            .forEach { stage.addActor(it) }

        (snacks + toys)
            .forEach { stage.addActor(it) }
    }

    private fun updatePet(action: () -> Pet) {
        try {
            val newPet = action()
            if (pet != newPet) {
                removePetFromStage()
                pet = newPet
                addPetToStage()
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun actOnPetActorsExceptEntity(action: (actor: Actor) -> Unit) {
        pet.actors
            .filter { (key, _) -> key != "entity" } // This will bite me in the ass later
            .forEach { (_, actor) -> action(actor) }
    }

    private fun addPetToStage() {
        actOnPetActorsExceptEntity { stage.addActor(it) }
    }

    private fun removePetFromStage() {
        actOnPetActorsExceptEntity { stage.removeActor(it) }
    }

    private fun generateCategoryItem(category: ItemCategory): Actor {
        val itemName = Item.assetsByCategoryMapAndName[category]!!.keys.random()

        return generateItemActor(itemName)
    }

    private fun generateItemActor(itemName: String): Actor {
        val category = Item.categoryByName[itemName]!!
        val item = Item(itemName, category, 1)

        val actor = Actor(Item.assetsByCategoryMapAndName[category]!![itemName]!!, (0..WorldConstants.STAGE_WIDTH).random() - 80, (50..WorldConstants.STAGE_HEIGHT).random() - 80)
        actor.reactionForMouseClick = {
            stage.removeActor(actor)
            updatePet { pet.handleItem(item) }
        }

        return actor
    }
}


