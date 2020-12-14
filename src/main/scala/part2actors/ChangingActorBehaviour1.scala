package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ChangingActorBehaviour1.Counter1.{Decrement, Increment, Print}
import part2actors.ChangingActorBehaviour1.FussyKid1.{Accept, Reject}
import part2actors.ChangingActorBehaviour1.Mom1.{Ask, CHOCOLATE, Food, Start, VEGETABLES}


object ChangingActorBehaviour1 extends App {

  object FussyKid1 {

    case object Accept

    case object Reject

    val HAPPY = "happy"
    val SAD = "sad"
  }

  class FussyKid1 extends Actor {

    import FussyKid1._
    import Mom1._

    var state = HAPPY

    override def receive: Receive = {
//      case Food(str) =>
//        if (str.equals(VEGETABLES))
//          state = HAPPY
//        else {
//          state = SAD
//        }

      case Food(VEGETABLES) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask =>
        if (state.equals(HAPPY))
          sender() ! Accept
        else
          sender() ! Reject
    }
  }

  object Mom1 {

    case class Start(kid: ActorRef)

    case class Food(food: String)

    case object Ask

    val VEGETABLES = "vegetables"
    val CHOCOLATE = "chocolate"
  }

  class Mom1 extends Actor {

    import Mom1._
    import FussyKid1._

    override def receive: Receive = {
      case Start(kid) =>
        kid ! Food(CHOCOLATE)
        kid ! Ask
      case Accept => println("My kid is happy")
      case Reject => println("My kid is sad")

    }
  }

  val system = ActorSystem("ChangingActorBehaviour1")
  val fussyKid = system.actorOf(Props[FussyKid1])
  val mom = system.actorOf(Props[Mom1])
  mom ! Start(fussyKid)

  class FussyKindStateless1 extends Actor{
    override def receive: Receive = HappyReceive

    def HappyReceive :Receive={
      case Food(CHOCOLATE) =>
      case Food(VEGETABLES) => context.become(SadReceive)
      case Ask => sender() ! Accept
    }

    def SadReceive:Receive={
      case Food(VEGETABLES) =>
      case Food(CHOCOLATE) => context.become(HappyReceive)
      case Ask => sender() ! Reject
    }
  }

  val fussyKidStateles1 = system.actorOf(Props[FussyKindStateless1])
  mom ! Start(fussyKidStateles1)

  // Counter made stateless
  object Counter1{
    case object Increment
    case object Decrement
    case object Print
  }

  class Counter1 extends Actor{
    override def receive: Receive = CountHoder(0)
    def CountHoder(count: Int) : Receive={
      case Increment =>
        context.become(CountHoder(count+1))
        println("count = "+count)
      case Decrement =>
        context.become(CountHoder(count-1))
        println("count = "+count)
      case Print =>
        println("count is now: "+count)
    }
  }

  val counter1=system.actorOf(Props[Counter1])
  (1 to 5).foreach(int => counter1 ! Increment)
  (1 to 3).foreach(int => counter1 ! Decrement)
  counter1 ! Print

}
