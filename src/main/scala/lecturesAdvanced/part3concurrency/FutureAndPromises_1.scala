package lecturesAdvanced.part3concurrency

import org.scalatest.concurrent.SocketInterruptor

import scala.concurrent.Await._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}

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

  // functional composition of future (map, flatmap and filter) these are applicable of a future only
  // they all return a future of the same type, except for map which returns a future of another type.
  val mark = SocialNetwork.fetchProfile("one")
  val nameOfMark = mark.map(profile => profile.name)
  val idOfMark = mark.map(profile => profile.id)
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
  // returns objects that are extracted from futures. It extracts objects from futures automatically.
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

  // online banking app
  case class User(name: String)

  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "Rock the JVM banking"

    def fetchUser(name: String): Future[User] = Future {
      // simulate fetching from the DB
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      // simulate some process
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCESS")
    }

    def purchase(userName: String, item: String, merchantName: String, cost: Double): String = {
      // fetch the user from DB
      // create a transaction from the user to the merchant
      // wait for the transaction to finish
      val transactionStatusFuture = for {
        user <- fetchUser(userName)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status
      // this is how you block a future. i.e. wait for it until it either finishes processing or the its timeout period runs out.
      Await.result(transactionStatusFuture, 2.seconds) // implicit conversions -> pimp my library
    }
  }

  // calling the purchase method in the following line will block until all the futures are completed
  println(BankingApp.purchase("Daniel", "Iphone 12", "rock the jvm store", 3000))

  // ---------------------
  // promises
  // sometimes we need to set or complete a future at a point of our choosing which
  // introduces the need or concept of promises.
  // a promise is a controller over future
  val promise = Promise[Int]() // this calls the apply method of the promise companion object
  val future = promise.future

  // here we rewrite the concept of producer/consumer using promises
  // thread 1 - "consumer"
  future.onComplete {
    case Success(r) => println("[consumer] I have received+" r)
  }

  // thread 2 -"producer"
  val producer = new Thread(() => {
    println("[producer] crunching numbers")
    Thread.sleep(500)
    // fulfilling the promise
    // you can fulfill a promies by either one of the three following ways:
    // 1. promise.success(result) as is the case in this example
    // 2. promise.failure(new BadException())
    // 3. promise.complete(Try{...})
    promise.success(42) // this manipulates the internal future to complete with a successful value of 42, which is then handeled by onComplete pm some consumer thread.
    println("[producer] done")
  })

  producer.start()
  Thread.sleep(1000)

  /*
    1) fulfill a future immediately with a value
    2) inSequence(fa,fb)
    3) first(fa,fb) => new future with the first value of the two futures
    4) last(fa,fb) => new future with the last value
    5) retryUntil(action: () => Future[T], condition: T => Boolean): Future[T]
   */

  // 1 - fulfill immediately
  def fulfillImmediately[T](value: T): Future[T] = Future(value)

  // 2 - insequence
  def insequence[A, B](first: Future[A], second: Future[B]): Future[B] = {
    first.flatMap(_ => second) // this runs the second future after having made sure that the first is completed
  }

  // 3 - first out of two futures
  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A]

    //    def tryComplete(promise: Promise[A], result: Try[A]) = result match {
    //      case Success(r) => try {
    //        promise.success(r)
    //      } catch {
    //        case _ =>
    //      }
    //      case Failure(t) => try {
    //        promise.failure(t)
    //      } catch {
    //        case _ =>
    //      }
    //    }

    // when two or more futures try to fulfill a promise, use promise.tryComplete to avoid an exception being throwed.

    //    fa.onComplete(result => tryComplete(promise, _))    // or in another way:
    fa.onComplete(promise.tryComplete)
    //    fb.onComplete(result => tryComplete(promise, _))      // or in another way:
    fb.onComplete(promise.tryComplete)

    promise.future // at the end, promise.future will hold either a value or the exception returned by the first
    // firnished promise out of these two
  }

  // 4 - last out of the two futures
  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // 1 promise which both futures will try to complete
    // 2 promise which the last future will complete
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]
    val checkAndComplet = (result: Try[A]) => {
      if (!bothPromise.tryComplete(result))
        lastPromise.complete(result)
    }

    fa.onComplete(checkAndComplet)
    fb.onComplete(checkAndComplet)

    lastPromise.future
  }

  val fast = Future {
    Thread.sleep(100)
    42
  }

  val slow = Future {
    Thread.sleep(200)
    45
  }

  first(fast, slow).foreach(f => println("first " + f))
  last(fast, slow).foreach(l => println("last " + l))
  Thread.sleep(1000)

  // retry until
  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] =
    action().filter(condition).recoverWith {
      case _ => retryUntil(action, condition)
    }

  val random = new Random()
  val action = () => Future {
    Thread.sleep(100)
    val nextValue = random.nextInt(100)
    println("generated " + nextValue)
    nextValue
  }

  retryUntil(action, (x: Int) => x < 10).foreach(result => println("settled at " + result))
  Thread.sleep(10000)
}
