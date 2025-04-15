import de.th_koeln.basicstage.Actor
import de.th_koeln.basicstage.Stage
import de.th_koeln.basicstage.coordinatesystem.WorldConstants
import de.th_koeln.imageprovider.Assets
import kotlin.properties.Delegates

fun <T : Actor> invokeOnClick(actor: T, func: ()->Unit): T {
    val cur = actor.reactionForMouseClick

    actor.reactionForMouseClick = {
        cur?.invoke()

        func.invoke()
    }

    return actor
}

fun <T : Actor> fadeOutAndRemoveOnClick(actor: T, stage: Stage): T {
    val cur = actor.reactionForMouseClick

    actor.reactionForMouseClick = {
        cur?.invoke()

        println("${actor.toString()} has been clicked!")

        actor.animation.queue.addPropertyAnimation(
            PropertyAnimationValueChange(
                _start = actor.opacity,
                _end = 0,
                numberOfSteps = 10,
                actor = actor,
                propertyName = AnimatableProperties.OPACITY
            )
        )

        // dirty hack
        // Gotta have some fucking way to detect once the fade out animation has run.
        // So we MOVE the damn thing after the animation, causing it to leave the mouse courser, which in turn is our signal to remove us
        actor.animation.queue.addPropertyAnimation(
            PropertyAnimationValueChange(
                _start = actor.x,
                _end = 9999,
                numberOfSteps = 1,
                actor = actor,
                propertyName = AnimatableProperties.X
            )
        )
        actor.reactionForMouseExited = {
            println("${actor.toString()} has died!")
            stage.removeActor(actor)
        }
    }

    return actor
}

fun main() {
    println("Hello World!")

    val stage = Stage()
    val score = Actor(Assets.EMPTY, WorldConstants.STAGE_WIDTH - 80, 0, 175, 30) // Need both, width AND heightInit?!
    val resetButton = Actor(Assets.EMPTY, 0, 0, 90, 45)
    val kodee = Actor(Assets.KODEE, WorldConstants.STAGE_WIDTH / 2, WorldConstants.STAGE_HEIGHT / 2)
    val snackCount = 15

    score.text.content = "0"
    resetButton.text.content = "RESET"
    resetButton.text.textBackground = Assets.textBackgrounds.SIMPLE_BUTTON

    var snacksEaten: Int by Delegates.observable(0) {property, old, new ->
        if (new > 4) {
            score.text.content = "Ich bin satt - $new"
        }
        else {
            score.text.content = new.toString()
        }

        if (new == snackCount) {
            kodee.animation.queue.addPropertyAnimation(
                PropertyAnimationValueChange(
                    _start = 0,
                    _end = 360,
                    numberOfSteps = 20,
                    actor = kodee,
                    propertyName = AnimatableProperties.ROTATION
                )
            )
        }
    }

    // TODO: Clean this up / Partially applied functions
    fun generateSnacks(arrangement: Arrangement = Arrangement.RANDOM): Sequence<Actor> {
        val cultivatedSnackAssets = listOf(Assets.snacks.SALAD, Assets.snacks.BOWL, Assets.snacks.DONUT1, Assets.snacks.DONUT2, Assets.snacks.ICE_CREAM, Assets.snacks.COFFEE, Assets.snacks.PIZZA, Assets.snacks.RAMEN) // WHY IS Assets.snacks NOT A LIST

        val seq: Sequence<Actor>

        when (arrangement) {
            Arrangement.ROW -> {
                val y = (0..WorldConstants.STAGE_HEIGHT).random()
                seq = generateSequence{ Actor(cultivatedSnackAssets.random(), (0..WorldConstants.STAGE_WIDTH).random(), y, ) }
            }
            Arrangement.COLUMN -> {
                val x = (0..WorldConstants.STAGE_WIDTH).random()
                seq = generateSequence{ Actor(cultivatedSnackAssets.random(), x, (0..WorldConstants.STAGE_HEIGHT).random()) }
            }
            else -> seq = generateSequence{ Actor(cultivatedSnackAssets.random(), (0..WorldConstants.STAGE_WIDTH).random(), (0..WorldConstants.STAGE_HEIGHT).random()) }
        }

        return seq
            .take(snackCount)
            .map { fadeOutAndRemoveOnClick(it, stage) }
            .map { invokeOnClick(it) { snacksEaten += 1 } }
    }
    val snacks: List<Actor> = generateSnacks().toList()

    resetButton.reactionForMouseClick = {
        snacks.forEach { it.animation.queue.addPropertyAnimation(
            PropertyAnimationValueChange(
                _start = (0..700).random(),
                _end = (0..700).random(),
                numberOfSteps = 15,
                actor = it,
                propertyName = listOf(AnimatableProperties.X, AnimatableProperties.Y).random()
            )
        ) }
    }

    stage.addActor(score)
    stage.addActor(resetButton)
    snacks.forEach { stage.addActor(it) }
    stage.addActor(kodee)
}


