package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.Actorcapabilities1.Perosning.LiveTheLife


object Actorcapabilities1 extends App {

  val system = ActorSystem("actorCapabilities_Exercise")

  object Counter1 {

    case object Increment

    case object Decrement

    case object Print

  }

  class Counter1 extends Actor {

    import Counter1._

    var counter = 0

    override def receive: Receive = {
      case Increment => counter += 1
      case Decrement => counter -= 1
      case Print => println(s"the recent value of counter = $counter")
    }
  }

  val count = system.actorOf(Props[Counter1], "SimpleCounter")

  import Counter1._

  (1 to 5).foreach(_ => count ! Increment)
  (1 to 3).foreach(_ => count ! Decrement)
  count ! Print

  // Bank account
  object BankAccounting {

    case class Deposite(amount: Int)

    case class Withdraw(amount: Int)

    case object Statement

    case class TransactionFailure(msg: String)

    case class TransactionSuccess(msg: String)

  }

  class BankAccounting extends Actor {

    import BankAccounting._

    var funds = 0

    override def receive: Receive = {
      case Deposite(amount) => {
        if (amount <= 0) sender() ! TransactionFailure("this amount cannot be deposited")
        else {
          sender() ! TransactionSuccess(s"$amount has been deposited in your account")
          funds += amount
        }
      }
      case Withdraw(amount) => {
        if (amount <= 0) sender() ! TransactionFailure("this amount cannot be deposited")
        else if (amount > funds) sender() ! TransactionFailure("this amount cannot be deposited")
        else {
          sender() ! TransactionSuccess(s"$amount has been deposited in your account")
          funds -= amount
        }
      }
      case Statement => sender() ! s"the actual balance of your account is: $funds"
    }
  }

  object Perosning {

    case class LiveTheLife(account: ActorRef)

  }

  class Perosning extends Actor {

    import Perosning._
    import BankAccounting._

    override def receive: Receive = {
      case LiveTheLife(account) => {
        account ! Deposite(5000)
        account ! Withdraw(6000)
        account ! Withdraw(500)
        account ! Statement
      }
      case message => println(message)
    }
  }

  val person1 = system.actorOf(Props[Perosning])
  val account = system.actorOf(Props[BankAccounting])
  person1 ! LiveTheLife(account)

}
