package playground

import akka.actor.ActorSystem

object Playground extends App{
  val actorSystem=ActorSystem("HalloAkka")
  println(actorSystem.name)
}
