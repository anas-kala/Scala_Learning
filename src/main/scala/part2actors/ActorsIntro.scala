package part2actors

import akka.actor.{Actor, ActorSystem, Props}

object ActorsIntro extends App {

  // part1 - actor systems
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name)

  // part2 - create actors in the actor system
  // word count actor
  class WordCountActor extends Actor {
    // internal data
    var totalWords = 0

    // behaviour
    override def receive: PartialFunction[Any, Unit] = {
      case message: String =>
        println(s"[word counter] I have received: $message")
        totalWords += message.split(" ").length
      case msg => println(s"[word counter] I cannot understand ${msg.toString}")
    }
  }

  // part3 - instantiate our actor
  // actors are unique, they must have different names
  val wordCounter = actorSystem.actorOf(Props[WordCountActor], "wordCounter")
  val anotherWordCounter=actorSystem.actorOf(Props[WordCountActor],"anotherWordCounter")
  val coco=actorSystem.actorOf(Props[WordCountActor],"coco")
  // part4 - communicate with the actor
  //  wordCounter.!("I am learning Akka and it is pretty daml cool"), or
  wordCounter ! "I am learning Akka and it is pretty daml cool"
  anotherWordCounter ! "A different message"
  coco ! 456
  // sending the message is asynchronous

  class Person(name:String) extends Actor{
    override def receive: Receive ={      // Receive here is equivalent to PartialFunction[Any,Unit]
      case "hi" => println(s"Hi, my name is $name")
      case _ =>
    }
  }
  // creating props with actor instances (this way is discouraged)
  val person= actorSystem.actorOf(Props(new Person("Bob")))
  person ! "hi"

  // the ideal way of creating props with actor instances is the following, especially when you want to
  // pass a constructor argument is to declar a companion object, and inside it we define some methods
  // that return prop objects. like the following

  object Person{
    def props(name:String) =Props(new Person(name))   // this is a factory method
  }
   // now we create props like this:
  val person1=actorSystem.actorOf(Person.props("Bob"))
  person1 ! "hi"

}
