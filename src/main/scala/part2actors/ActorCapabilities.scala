package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ActorCapabilities.Counter.{Derement, Increment, Print}
import part2actors.ActorCapabilities.Person.LiveTheLife

object ActorCapabilities extends App {

  val system = ActorSystem("actorCapabilitiesDemo")

  class SimpleActor extends Actor {
    override def receive: Receive = {
      case "Hi" => context.sender() ! "Hello there"   // replying to a message
      case message: String => println(s"[$self] I have received $message")
      case number: Int => println(s"[simple actor] I have received a Number $number")
      case SpecialMessage(contents) => println(s"[simple actor] I have received something special: $contents")
      case SendMessageToYourself(content) => self ! content   // send message to yourself, which is in turn a string and
                                                              // will trigger the first case
      case SayHiTo(ref) => ref ! "Hi"
      case WirelessPhonjoeMessage(content, ref) => ref forward(content+"s") // i keep the original sneder of the WPM
    }
  }

  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")
  simpleActor ! "hello, actor"

  // 1 - messages can be of any type under two conidtions
  // a) messages must be immutable
  // b) messages must be serializable
  // in practice use case classes and case objects to ensure b). for the first condition a) we will have to take care of it
  simpleActor ! 42
  case class SpecialMessage(contents: String)
  simpleActor ! SpecialMessage("some special content")

  // 2 - actors have information about their context and about themselves
  // context.self === this in oop

  case class SendMessageToYourself(content:String)
  simpleActor ! SendMessageToYourself("I am an actor and I am proud of it")

  // 2 - actors can reply to messages
  val alice=system.actorOf(Props[SimpleActor],"alice")
  val anas=system.actorOf(Props[SimpleActor],"anas")
  val bob=system.actorOf(Props[SimpleActor],"bob")

  case class SayHiTo(ref: ActorRef)
  anas ! SayHiTo(bob)

  // 4 - if there is no sender, the reply will go to dead letters
  alice ! "Hi"    // no sender

  // 5 - forwarding messages
  // D -> A -> B    this is called forwarding = sending a message with the original sender
  // with forwarding, you keep the original sender
  case class WirelessPhonjoeMessage(content: String, ref:ActorRef)
  alice ! WirelessPhonjoeMessage("Hi",bob)  //noSender

  /**
   * Exerciese
   * 1. a Counter actor
   *    - Increment
   *    - Decrement
   *    - Print
   *
   * 2. Bank account as an actor
   *    receives
   *    - Deposit an amount
   *    - Withdraow an amount
   *    - Statement
   *    replies with
   *    - Success
   *    - Failure
   *
   *    interact with some other kind of actor
   */

  // first exercise
  object Counter {

    case object Increment

    case object Derement

    case object Print

  }
  class Counter extends Actor{    // as a best practice put your messages or the messages that this actor supports in its companion object.
    import Counter._
    var count=0
    override def receive: Receive={
      case Increment => count+=1
      case Derement => count-=1
      case Print => println(s"[counter] My current count is $count ")
    }
  }

  val counter=system.actorOf(Props[Counter],"myCounter")

  (1 to 5).foreach(_ => counter ! Increment)
  (1 to 3).foreach(_ => counter ! Derement)
  counter ! Print

  // second exercise Bank Account
  object BankAccount{
    case class Deposit(amount: Int)
    case class Withdraw(amount: Int)
    case object Statement
    case class TransactionSuccess(message:String)
    case class TransactionFailure(reson:String)
  }
  class BankAccount extends Actor{
    import BankAccount._
    var funds=0

    override def receive: Receive = {
      case Deposit(amount) => {
        if(amount <0) sender() ! TransactionFailure("invalid deposite amount")
        else {
          funds +=amount
          sender() ! TransactionSuccess(s"successfully deposited amount $amount")
        }
      }
      case Withdraw(amount) =>{
        if(amount<0) sender ! TransactionFailure("invalid withdraw amount")
        else if (amount>funds)
          sender() ! TransactionFailure("insufficient funds")
        else{
          funds -=amount
          sender() ! TransactionSuccess(s"successfully withdrew $amount")
        }
      }
      case Statement => sender() ! s"Your balance is $funds"

    }
  }

  // the following class (Person) will be used to interact with the bank account
  object Person{
    case class LiveTheLife(account: ActorRef)
  }
  class Person extends Actor{
    import Person._
    import BankAccount._

    override def receive: Receive = {
      case LiveTheLife(account) =>{
        account ! Deposit(1000)
        account ! Withdraw(90000)
        account ! Withdraw(500)
        account ! Statement
      }
      case message => println(message.toString)
    }
  }

  val account=system.actorOf(Props[BankAccount],"bankAccount")
  val person=system.actorOf(Props[Person],"billionair")

  person ! LiveTheLife(account)
}
