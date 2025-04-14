import de.th_koeln.basicstage.Actor
import de.th_koeln.basicstage.Stage

fun helloWorldBuilder (): String {
    return "Hello World!"
}

 fun main () {
     val stage = Stage()
     val actor = Actor()

     println(helloWorldBuilder())

     stage.addActor(actor)
 }