class Health(energy: Int, fitness: Int) {
    var energy: Int = 0
        set(value) {
            field = when {
                value >= 0 -> value
                else -> 0
            }
        }

    var fitness: Int = 0
        set(value) {
            field = when {
                value >= 0 -> value
                else -> 0
            }
        }

    init {
        this.energy = energy
        this.fitness = fitness
    }
}