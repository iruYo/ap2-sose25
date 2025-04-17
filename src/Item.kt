import de.th_koeln.basicstage.Actor
import de.th_koeln.basicstage.coordinatesystem.WorldConstants
import de.th_koeln.imageprovider.Assets



class Item(name: String, val category: ItemCategory, amount: Int) {
    companion object {
        private val zeroSeq = generateSequence { 0 }.take(50).toList() // ugh

        private val snackNames: List<String> = listOf("SALAD", "BOWL", "DONUT1", "DONUT2", "ICE_CREAM", "COFFEE", "PIZZA", "RAMEN")
        private val snackEnergy: List<Int> = listOf(5, 10, 10, 5, 1, 15, 5, 2)
        private val snackHappiness: List<Int> = zeroSeq.take(snackNames.size)
        private val snackAssets: List<String> = listOf(Assets.snacks.SALAD, Assets.snacks.BOWL, Assets.snacks.DONUT1, Assets.snacks.DONUT2, Assets.snacks.ICE_CREAM, Assets.snacks.COFFEE, Assets.snacks.PIZZA, Assets.snacks.RAMEN)

        private val toyNames: List<String> = listOf("BOAT", "DRUMS", "RUBBER_DUCK", "TEDDY", "TRAIN", "BALLONS", "SPINNER")
        private val toyEnergy: List<Int> =  zeroSeq.take(toyNames.size)
        private val toyHappiness: List<Int> = listOf(5, 10, 10, 5, 1, 15, 5)
        private val toyAssets: List<String> = listOf(Assets.toys.BOAT, Assets.toys.DRUMS, Assets.toys.RUBBER_DUCK, Assets.toys.TEDDY, Assets.toys.TRAIN, Assets.toys.BALLONS, Assets.toys.SPINNER)

        private val otherNames: List<String> = listOf("OTHER")
        private val otherAssets: List<String> = listOf(Assets.misc.UFO)
        private val otherEnergy: List<Int> = zeroSeq.take(otherNames.size)
        private val otherHappiness: List<Int> = zeroSeq.take(otherNames.size)

        private val snackAssetsByName = (snackNames zip snackAssets).toMap()
        private val toyAssetsByName = (toyNames zip toyAssets).toMap()
        private val otherAssetsByName = (otherNames zip otherAssets).toMap()

        private val snackHappinessByName = (snackNames zip snackHappiness).toMap()
        private val toyHappinessByName = (toyNames zip toyHappiness).toMap()
        private val otherHappinessByName = (otherNames zip otherHappiness).toMap()

        private val snackEnergyByName = (snackNames zip snackEnergy).toMap()
        private val toyEnergyByName = (toyNames zip toyEnergy).toMap()
        private val otherEnergyByName = (otherNames zip otherEnergy).toMap()

        val assetsByCategoryMapAndName = mapOf(ItemCategory.FOOD to snackAssetsByName, ItemCategory.TOY to toyAssetsByName, ItemCategory.OTHER to otherAssetsByName)
        val happinessValueByCategoryMapAndName = mapOf(ItemCategory.FOOD to snackHappinessByName, ItemCategory.TOY to toyHappinessByName, ItemCategory.OTHER to otherHappinessByName)
        val energyByCategoryMapAndName = mapOf(ItemCategory.FOOD to snackEnergyByName, ItemCategory.TOY to toyEnergyByName, ItemCategory.OTHER to otherEnergyByName)

    }

    val happinessImpact: Int = happinessValueByCategoryMapAndName[category]!![name]!!
    val energyImpact: Int = energyByCategoryMapAndName[category]!![name]!!

    var onActorClick: (actor: Actor) -> Unit = { println("noop") }
    var actors: List<Actor> = emptyList()

    init {
        actors = (1..amount)
            .map { Actor(assetsByCategoryMapAndName[category]!![name]!!, (0..WorldConstants.STAGE_WIDTH).random() - 80, (50..WorldConstants.STAGE_HEIGHT).random() - 80) }
            .map {
                it.reactionForMouseClick = {
                    onActorClick(it)
                }
                it
            }
    }
}