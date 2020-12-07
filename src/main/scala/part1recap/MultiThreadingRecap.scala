package part1recap

import scala.concurrent.Future
import scala.util.{Failure, Success}

object MultiThreadingRecap extends App {

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("I'm running in parallel")
  })

  // Syntactic sugar for this is
  val bThread = new Thread(() => println("I'm running in parallel"))

  //starting and waiting for a thread to finish is
  aThread.start()
  aThread.join()

  val threadHello = new Thread(() => (1 to 100).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 100).foreach(_ => println("goodBey")))
  threadGoodbye.start()
  threadHello.start()

  // the problem with thread is that different runs produce different results.

  //there are two ways to make a variable or method thread safe
  //using synchronized
  //using @volatile, but the problem with this is that it is only applicable to variables of primitive types.
  class BanAccount(@volatile var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int) = this.amount -= money

    def safeWithdraw(money: Int) = this.synchronized { //with synchronized only one thread accesses this method at a time
      this.amount -= money
    }
  }

  /*
  BA (10000)
  T1 -> withdraw 1000
  T1 -> withdraw 2000

  T1 -> this.amount =this.amount -.... // preempted by OS
  T2 -> this.amount =this.amount -2000=8000
  T1 -> -1000=9000

  result = 9000   not thread safe (it is not atomic) (atomic = only one thread is allowed to execute the function at a time)
   */

  // inter-thread communication on the JVM
  // wait - notify mechanism

  // Scala Futures

  import scala.concurrent.ExecutionContext.Implicits.global

  val future = Future {
    // long computation - on a different thread
    42
  }

  // with future you can use callbacks, as follows:
  future.onComplete {
    case Success(42) => println("I found the meaning of life")
    case Failure(_) => println("something happened with the meaning of life")
  }

  val aProcessedFuture = future.map(_ + 1) //future with 43
  val aFlatFuture = future.flatMap({ value => Future(value + 2) })  //Future with 44
  val filteredFuture=future.filter(_%2==0)  //NoSuchElementException

  // futures support for comprehensions
  val aNonsenseFuture=for{
    meaningOfLife <- future
    filteredMeaning <- filteredFuture
  } yield (meaningOfLife+filteredMeaning)

  // with future you can also use recover and recoverWith

  // writable futures are promises
}
