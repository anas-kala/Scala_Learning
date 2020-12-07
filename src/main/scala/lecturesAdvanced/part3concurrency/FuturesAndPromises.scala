package lecturesAdvanced.part3concurrency

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success}

object FuturesAndPromises extends App {
  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  // to do the preceding thread using futures:
  val aFuture = Future {
    calculateMeaningOfLife // calculates the meaning of life on another thread
  } // after importing (global) it is passed by the compiler

  println(aFuture.value) // Option[Try[Int]]

  println("Waitnig on the future")
  aFuture.onComplete {
    case Success(meaningOfLife) => println(s"the meaning of life is going to be $meaningOfLife")
    case Failure(e) => println(s"I have failed with ${e}")
  } // some thread

  Thread.sleep(3000)

  // mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) = {
      println(s"${this.name} poking ${anotherProfile.name}")
    }
  }


  object SocialNetwork {
    // "database"
    val name = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )
    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )
    val random = new Random()

    //API
    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      Profile(id, name(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, name(bfId)) // this will create a pforile out of bfId and name(bfId)
    }
  }

  // client: mark to poke bill
  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")     // this is nested future, which is not reliable
//  mark.onComplete {
//    case Success(markProfile) => {
//      val bill = SocialNetwork.fetchBestFriend(markProfile)
//      bill.onComplete {
//        case Success(billProfile) => markProfile.poke(billProfile)
//        case Failure(e) => e.printStackTrace()
//      }
//    }
//    case Failure(ex) => ex.printStackTrace()
//  }

//  Thread.sleep(1000)    // this thread is to make sure that the future from SocialNetwork has time to finish.


  // functional composition of futures
  // map, flatmap, filter
  // map turns the type of given future into another type
  val nameOnTheWall=mark.map(profile => profile.name)   // mark here is a future
                                                        // here we turned transitioned from a future of type Profile
                                                        // to a future of type string.
                                                        // when the the future mark fails with an exception, the
                                                        // the future nameOnTheWall will fail with the same exception

  // flatMap turns the a form of future[profile] into another future[profile] but in another form
  val marksBestFriend =mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))

  // the filter filters the future. and returns the same type of future but using some filter
  // if the main future fails ( if marksBestFriend fials) the exception is NoSuchElementException
  val zuckBestFriendRestrichted=marksBestFriend.filter(profile => profile.name.startsWith("Z"))


  // with map, flatMap and filter you can write for-comprehension
  // the following substitutes the nested future statement in the lines 62 -> 71
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  // fallbacks for recovering a future
  // for example, the following call will throw an exception. in order to recover this future, we use callback as follows
  val aProfileNoMatterWhat=SocialNetwork.fetchProfile("unknown id").recover{
    // here we pass a partial function with an exception
    case e: Throwable => Profile("fb.id.0-dummy", "Forever alone")
  }
  // by doing so, we make sure that aProfileNoMatterWhat will either be the real profile returned from the id
  // that exists. or a dummy profile if the id does not exist.
  // in the case we do not want to recover a dummy profile but rather another real profile, we use recoverWith,
  // but here we should pass an id that we are really sure it exists so that we do not get another exception
  // like the following
  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith{
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.2-bill")
  }

  // fallbackTo
  // the logic of fallbackTo works as follows:
  // if the future of the first argument succeeds (SocialNetwork.fetchProfile("unknown id"))
  // if the first argument fails, the future of the next argument will be returned
  // if the second future also fails, the exception of the first argument will be returned.
  val fallbckResult=SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.2-bill"))
}
