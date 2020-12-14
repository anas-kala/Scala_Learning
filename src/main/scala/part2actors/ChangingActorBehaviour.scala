package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.ChangingActorBehaviour.Counter.Decrement
//import part2actors.Actorcapabilities1.Counter1.Decrement
import part2actors.ChangingActorBehaviour.Counter.{Increment, Print}
import part2actors.ChangingActorBehaviour.Mom.MomStart

object ChangingActorBehaviour extends App {

  object FussyKid {

    case object KidAccept

    case object KidReject

    val HAPPY = "happy"
    val SAD = "sad"
  }

  class FussyKid extends Actor {

    import FussyKid._
    import Mom._

    // internal state of Kid
    var state = HAPPY

    override def receive: Receive = {

      case Food(VEGETABLE) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(_) =>
        if (state == HAPPY) sender() ! KidAccept
        else sender() ! KidReject
    }
  }

  object Mom {

    case class MomStart(kidRef: ActorRef) // sending this message to mom (from outside or from main method) will trigger her to start sending messages to her son
    case class Food(food: String)

    case class Ask(message: String) // do you want to play
    val VEGETABLE = "vegetables"
    val CHOCOLATE = "chocolate"
  }

  class Mom extends Actor {

    import Mom._
    import FussyKid._

    override def receive: Receive = {
      case MomStart(kidRef) =>
        // test our interaction
        kidRef ! Food(VEGETABLE)
        kidRef ! Ask("do you want to play?")
      case KidAccept => println("My kid is happy")
      case KidReject => println("My kid is sad")
    }
  }

  val system = ActorSystem("changingActorBehaviourDemo")
  val fussyKid = system.actorOf(Props[FussyKid])
  val mom = system.actorOf(Props[Mom])

  mom ! MomStart(fussyKid)

  println("--------------")

  // the above implementation is pretty bad, because the variable that leads to the actor changing its behaviour
  // might be very complex. in this case the receive() method of the kid will be very long.
  // another reason for this implementation being bad is the fact that we used variables. normally
  // in actors we use something that is less mutable that variables.

  class StatelessFussyKid extends Actor {

    import FussyKid._
    import Mom._

    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(VEGETABLE) => context.become(sadReceive) //change my receive handler to sadReceive
      case Food(CHOCOLATE) =>
      case Ask(_) => sender() ! KidAccept
    }

    def sadReceive: Receive = {
      case Food(VEGETABLE) => // stay sad
      case Food(CHOCOLATE) => context.become(happyReceive) //change my receive handler to happyReceive
      case Ask(_) => sender() ! KidReject
    }
  }

  val statelessFussyKid = system.actorOf(Props[StatelessFussyKid])
  mom ! MomStart(statelessFussyKid)

  /**
   * Exercise
   * 1 - recreate the counter actor with context.become and no mutable state
   *
   */

  object Counter {

    case object Increment

    case object Decrement

    case object Print

  }

  // why rewriting the Counter Actor as follows???
  // because we want to transition from a stateful (mutable) case to stateless (immutable) case, which is the right practice.
  class Counter extends Actor {

    import Counter._

    override def receive: Receive = countReceive(0)

    def countReceive(currentCount: Int): Receive = {
      case Increment =>
        println(s"[countReceive($currentCount)] increment")
        context.become(countReceive(currentCount + 1))
      case Decrement => context.become(countReceive(currentCount - 1))
        println(s"[countReceive($currentCount)] decrement")
      case Print => println(s"[courentReceive($currentCount)] my current count is $currentCount")
    }
  }

  val counter = system.actorOf(Props[Counter], "myCounter")

  (1 to 5).foreach(_ => counter ! Increment)
  (1 to 5).foreach(_ => counter ! Decrement)
  counter ! Print

  /**
   * Exercise 2 - a simplified voting system
   */

  case class Vote(candidate: String)    // a citizen votes to the candidate

  case object VoteStatusRequest   // vote aggregator requests the candidate for which each citizen has voted for.

  case class VoteStatusReply(candidate: Option[String])   // a citizen replays with the candidates for which they voted.

  class Citizen extends Actor {
    var candidate: Option[String] = None // because the citizens have not voted yet
    override def receive: Receive = {
      case Vote(c) => candidate = Some(c) // initialize candidate with some value
      case VoteStatusRequest => sender() ! VoteStatusReply(candidate)
    }
  }

  case class AggregateVotes(citizens: Set[ActorRef])    // aggregate the citizens who have voted.

  class VoteAggregator extends Actor {
    var stillWaiting: Set[ActorRef] = Set()
    var currentStates: Map[String, Int] = Map()   // this map maps each candidate with the number of votes they got.

    override def receive: Receive = {
      case AggregateVotes(citizens) =>
        stillWaiting = citizens
        citizens.foreach(citizenRef => citizenRef ! VoteStatusRequest)
      case VoteStatusReply(None) =>
        // a citizen has not voted yet
        sender() ! VoteStatusRequest // this message is sent to the citizen. this might end up in an infinite loop
      // this would happen when a voter does not vote at all. which does not happen here.
      case VoteStatusReply(Some(candidate)) =>
        val newStillWaiting = stillWaiting - sender()
        val currentVotesOfCandidate = currentStates.getOrElse(candidate, 0)   // if the candidate already exists in this map, return the number of people who voted to him. otherwise return 0.
        currentStates = currentStates + (candidate -> (currentVotesOfCandidate + 1))
        if (newStillWaiting.isEmpty) {    // if all people requested for their candidates have replied to the vote request sent from the VoteAggregator, then
          println(s"[aggregator] poll stats: $currentStates")
        } else {
          stillWaiting = newStillWaiting
        }

    }
  }

  val alice = system.actorOf(Props[Citizen])
  val bob = system.actorOf(Props[Citizen])
  val charlie = system.actorOf(Props[Citizen])
  val daniel = system.actorOf(Props[Citizen])

  alice ! Vote("Marin")
  bob ! Vote("Jonas")
  charlie ! Vote("Roland")
  daniel ! Vote("Roland")

  val voteAggregator = system.actorOf(Props[VoteAggregator])
  voteAggregator ! AggregateVotes(Set(alice, bob, charlie, daniel))

  /*
  print the status of the votes
  Martin -> 1
  Jonas  -> 1
  Roland  -> 2
   */
}


