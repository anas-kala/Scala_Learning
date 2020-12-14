package part2actors

import akka.actor.{Actor, ActorSystem, Props}

object ActorsIntro1 extends App {
  // part 1 - actor system
  val actorSystem=ActorSystem("firstActorSystem")

  // part 2 - create actors in the actor system
  class WordCountActor extends Actor{
    // internal data
    var totalWords=0

    // behaviour
    def receive :Receive={
      case message:String => {
        println(s"[word counter] I have received the message: $message")
        totalWords+=message.split(" ").length
      }
      case msg => println(s"[word counter] I cannot understand ${msg.toString}")
    }
  }

  // part3 - instantiate our actor
  val wordCounter=actorSystem.actorOf(Props[WordCountActor],"wordCounter")
  val anotherWordCounter=actorSystem.actorOf(Props[WordCountActor],"anotherWordCounter")
  val dummyWordCounter=actorSystem.actorOf(Props[WordCountActor],"dummyWordCounter")

  // part4 - communicate with the actor
  wordCounter ! "I am learning Akka and it is pretty damn cool"
  anotherWordCounter ! "a different message"
  dummyWordCounter ! 789

  // another Actor with constructor argument
  class Person(name:String) extends Actor{
    override def receive : Receive={
      case "Hi"=> println(s"Hi, my name is $name")
      case _ =>
    }
  }

  object Person{
    def props(name:String)=Props(new Person(name))
  }

  val person1=actorSystem.actorOf(Person.props("Bob"),"person1")
  val person2=actorSystem.actorOf(Person.props("Anas"),"person2")

  person1 ! "Hallo here"
  person2 ! "Hi"
}
