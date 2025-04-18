data class Health(val energy: Int, val fitness: Int) {
    init {
        require(energy >= 0) { "energy must be non-negative, but was $energy" }
        require(fitness >= 0) { "fitness must be non-negative, but was $fitness" }
    }
}