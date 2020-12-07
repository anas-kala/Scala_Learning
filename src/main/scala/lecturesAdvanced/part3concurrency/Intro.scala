package lecturesAdvanced.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {
  /*
  interface Runnable {
    public void run()
  }
   */
  // JVM threads
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })

  //  aThread.start()   // gives the signal to the JVM to start a JVM thread
  // create a JVM thread => OS thread
  //  aThread.join()  // blocks other threads until aThread finishes running

  //  val threadhello=new Thread(()=> (1 to 5).foreach(_ => println("hellow")))
  //  val threadGoodBye=new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  //  threadhello.start()
  //  threadGoodBye.start()
  // different runs produce different results


  // executors
  //  val pool =Executors.newFixedThreadPool(10)
  //  pool.execute(()=> println("something in the thread pool"))      // this will be run by one of the ten threads
  //                                                                  //the pool manages which. and you do not have to
  //                                                                  //care about starting and stopping threads
  //  pool.execute(()=> {
  //    Thread.sleep(1000)
  //    println("done after one second")
  //  })
  //
  //  pool.execute(()=>{
  //    Thread.sleep(1000)
  //    println("almost done")
  //    Thread.sleep(1000)
  //    println("done after 2 seconds")
  //  })

  // sutting down all the pools
  //  pool.shutdown()   // the pool will not accept any more actions after this shutdown
  //  pool.shutdownNow()    // this interrupts any sleeping thread that is running in the pool and throws an exception in
  // case there is a sleeping thread running in the pool (Exception = sleep interrupted)

  // here we have used the newFixedThreadPool. Executors have many other thread pools

  //  def runInParallel={
  //    var x=0
  //    val thread1=new Thread(()=>{
  //      x=1
  //    })
  //    val thread2=new Thread(()=>{
  //      x=2
  //    })
  //    thread1.start()
  //    thread2.start()
  //    println(x)
  //  }

  //  for(_ <- 1 to 100) runInParallel
  // this is called race condition.

//  class BankAccount(var amount: Int) {
//    override def toString: String = "" + amount
//  }
//
//  def buy(account: BankAccount, thing: String, price: Int) = {
//    account.amount -= price
//    println("I have bought " + thing)
//    println("my account is now " + account)
//  }
//
//  for(_ <- 1 to 10000)
//  {
//    val account = new BankAccount(50000)
//    val thread1 = new Thread(() => buy(account, "shoes", 3000))
//    val thread2 = new Thread(() => buy(account, "Iphone 12", 4000))
//    thread1.start()
//    thread2.start()
//    Thread.sleep(10)
//    if (account.amount != 43000)
//      println("AHA: " + account.amount)
//
//  }
//  // how to fix the problem of concurrency
//  // option #1: use synchroized()
//  def buySafe(account: BankAccount, thing: String, price: Int) = {
//    account.synchronized{
//      // no two threads can evaluate this at the same time
//      account.amount -= price
//      println("I have bought " + thing)
//      println("my account is now " + account)
//    }
//  }

  // option#2: use @volatile

  /**
   * Exercise
   * 1) Construct 50 "inception" threads
   *    thread1 -> thread2 -> thread3 -> ....
   *    println("helloe from thread #3")
   *
   *    in REVERSE ORDER
   *
   * 2) you have the following code
   * var x = 0
   * val threads =(1 to 100).map(_ => new Thread(() => x+=1))
   * threads.foreach(_.start())
   *
   *  1. what is the biggest value possible for x?
   *  2. what is the smallest value possible for x?
   *
   *  3) sleep fallacy
   *  you have the following code
   *  var message = ""
   *  val awsomeThread = new Thread(() => {
   *      Thread.sleep(1000)
   *      message = "Scala is awsome"
   *  })
   *
   *  message = "Scala sucks"
   *  awesomeThread.start()
   *  Thread.sleep(2000
   *  println(message)
   *
   *  1. what is the value of message?
   *     is it guaranteed?
   *     why?
   */

  def inceptionThreads(maxThreads: Int,i:Int =1):Thread =new Thread(() => {
    if(i<maxThreads){
      val newThread=inceptionThreads(maxThreads,i+1)
      newThread.start()
      newThread.join()
    }
    println(s"hello from thread $i")
  })

  inceptionThreads(50).start()

  var x=0
  val threadList=(1 to 100).map(_ => new Thread(()=>x+=1))
  threadList.map(elem =>{
    elem.start()
    elem.join()   // try commenting out this line to see the difference.
    println(x)
  })
}
