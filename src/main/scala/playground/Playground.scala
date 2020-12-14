package playground

import akka.actor.{Actor, ActorSystem, Props}
import playground.Playground.Kid.greet

import java.util.Scanner

object Playground {
  val actorSystem = ActorSystem("HalloAkka")

  object Kid {

    case class greet(message: String)

  }

  class Kid extends Actor {

    import Kid._

    override def receive: Receive = {
      case greet(message) => println(message)
    }
  }

  def abc(message:String) = {
    val kid = actorSystem.actorOf(Props[Kid])
    kid ! greet("good morning")
  }

  def main(args: Array[String]) {
    print("Enter a number: ")
    var hours = scala.io.StdIn.readInt()
    hours = hours + 1
    println("Your entry + 1 : " + hours)
    print("Enter a greeting message: ")
    val greeting = scala.io.StdIn.readLine()
    abc(greeting)
    System.out.println("hallo World " + Math.sqrt(25))
    System.out.println("the greeting is: "+greeting)
  }
}
