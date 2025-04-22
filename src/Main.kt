fun main() {
    val test = compareBy<String> { it }

    println(test.compare("Z", "B"))
}
