import de.th_koeln.basicstage.Actor

abstract class StageLayout(actors: List<Actor>) {
    internal val xMin: Int = actors.minOf { it.x }
    internal val xMax: Int = actors.maxOf { it.x + it.width}
    internal val yMin: Int = actors.minOf { it.y }
    internal val yMax: Int = actors.maxOf { it.y + it.height }
    private val totalWidth: Int = xMax - xMin
    private val totalHeight: Int = yMax - yMin

    open fun arrange(): List<Actor>  {
        TODO()
    }
}

class AlignTop(private val actors: List<Actor>) : StageLayout(actors) {
    override fun arrange(): List<Actor>  {
        return actors.onEach {
            it.y += yMin
        }
    }
}

class AlignLeft(private val actors: List<Actor>) : StageLayout(actors) {
    override fun arrange(): List<Actor> {
        return actors.onEach {
            it.x += xMin
        }
    }
}

class AlignRight(private val actors: List<Actor>) : StageLayout(actors) {
    override fun arrange(): List<Actor>  {
        return actors.onEach {
            it.x += xMax
        }
    }
}