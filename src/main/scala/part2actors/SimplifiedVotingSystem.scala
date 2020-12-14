package part2actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2actors.SimplifiedVotingSystem.Citizen1.Vote
import part2actors.SimplifiedVotingSystem.VoteAggregator1.AggregateVotes

object SimplifiedVotingSystem extends App {

  /**
   * Simplified voting system
   */

  object Citizen1 {

    case class Vote(Candidate: String)

    case class VoteStatusReply(candidate: Option[String])

  }

  class Citizen1 extends Actor {
    var candidate: Option[String] = None

    import Citizen1._
    import VoteAggregator1._

    override def receive: Receive = {
      case Vote(c) => candidate = Some(c)
      case VoteStatusRequest => sender() ! VoteStatusReply(candidate)
    }
  }

  object VoteAggregator1 {

    case object VoteStatusRequest

    case class AggregateVotes(citizens: Set[ActorRef])

  }

  class VoteAggregator1 extends Actor {
    var stillWaiting: Set[ActorRef] = Set()
    var currentStats: Map[String, Int] = Map()

    import Citizen1._
    import VoteAggregator1._

    override def receive: Receive = {
      case AggregateVotes(citizens) =>
        stillWaiting = citizens
        citizens.foreach(f => f ! VoteStatusRequest)
      case VoteStatusReply(None) =>
        sender() ! VoteStatusRequest
      case VoteStatusReply(Some(c)) =>
        val newStillWaiting = stillWaiting - sender()
        val currentVotesOfCandidate = currentStats.getOrElse(c, 0)
        currentStats = currentStats + (c -> (currentVotesOfCandidate + 1))
        if (newStillWaiting.isEmpty)
          println(currentStats)
        else
          stillWaiting = newStillWaiting
    }
  }

  val system = ActorSystem("Simplified_Voting_System")
  val alice = system.actorOf(Props[Citizen1])
  val bob = system.actorOf(Props[Citizen1])
  val charlie = system.actorOf(Props[Citizen1])
  val daniel = system.actorOf(Props[Citizen1])


  alice ! Vote("Marin")
  bob ! Vote("Jonas")
  charlie ! Vote("Roland")
  daniel ! Vote("Roland")


  val voteAggregator = system.actorOf(Props[VoteAggregator1])
  voteAggregator ! AggregateVotes(Set(alice, bob, charlie, daniel))

}
