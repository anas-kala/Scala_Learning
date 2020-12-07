package lecturesAdvanced.part3concurrency

import org.scalatest.concurrent.SocketInterruptor

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

object FutureAndPromises_1 extends App {

  def theMeaningOfLife: String = {
    Thread.sleep(1000)
    "Time is the most valuable"
  }

  val aFuture = Future {
    theMeaningOfLife
  }
  aFuture.onComplete {
    case Success(string) => println(s"the meaning of life is $string")
    case Failure(exception) => println(exception)
  }
  Thread.sleep(1500)

  // mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) = {
      println(s"${this.name} pokes ${anotherProfile.name}")
    }
  }

  object SocialNetwork {
    val name = Map(
      "one" -> "mark",
      "two" -> "Bill",
      "three" -> "Gummy",
      "three" -> "Dummy"
    )

    val friends = Map(
      "one" -> "three"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(400)
      Profile(id, name(id))
    }

    def fetchBestFrient(profile: Profile): Future[Profile] = Future {
      Thread.sleep(400)
      val bestFriendId = friends(profile.id)
      Profile(bestFriendId, name(bestFriendId))
    }
  }

  // functional composition of future
  val mark = SocialNetwork.fetchProfile("one")
  val nameOfMark = mark.map(profile => profile.name)
  val profileOfMarksFriend = mark.flatMap(profile => SocialNetwork.fetchBestFrient(profile))
  profileOfMarksFriend.onComplete {
    case Success(profile) => println("as string, the friend of mark " + profile.name)
    case Failure(e) => println(e.printStackTrace())
  }
  val marksBestFriendWithFilter = profileOfMarksFriend.filter(profile => profile.name.startsWith("D"))
  marksBestFriendWithFilter.onComplete {
    case Success(profile) => {
      println("matching D " + profile.name)
      println("hallo world")
    }
    case Failure(e) => println(e.printStackTrace())
  }

  // for-comprehension of future
  for {
    mark <- SocialNetwork.fetchProfile("one")
    bill <- SocialNetwork.fetchBestFrient(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  // fallback of future
  val profileNoMatterWhat = SocialNetwork.fetchProfile("four").recover {
    case e: Throwable => Profile("Zero_Id_for_non_Existing_IDs", "the given id does not exist")
  }
  onComplete(profileNoMatterWhat)
  Thread.sleep(500)

  val profileThatExists = SocialNetwork.fetchProfile("five").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("two")
  }
  onComplete(profileThatExists)
  Thread.sleep(500)

  val fallBackResult = SocialNetwork.fetchProfile("six").fallbackTo(SocialNetwork.fetchProfile("three"))
  onComplete(fallBackResult)
  Thread.sleep(500)

  private def onComplete(x: Future[Profile]) = {
    x.onComplete {
      case Success(profile) => println(profile.id)
      case Failure(e) => println(e.printStackTrace())
    }
  }
}
