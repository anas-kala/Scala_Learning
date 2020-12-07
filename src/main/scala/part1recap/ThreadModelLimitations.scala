package part1recap

import scala.concurrent.Future

object ThreadModelLimitations extends App {

  /*
  Daiel's rants
   */

  /**
   * DR #1: OOP encapsulation is only valid in the single threaded model
   */
  class BanAccount(private var amount: Int) {
    override def toString: String = "" + amount

    def withdraw(money: Int) = this.synchronized {
      this.amount -= money
    }

    def depoite(money: Int) = this.synchronized {
      this.amount += money
    }

    def getAmount = amount
  }

//  val account = new BanAccount(2000)
//  for (_ <- 1 to 100) {
//    new Thread(() => account.depoite(1)).start()
//  }
//  for (_ <- 1 to 100) {
//    new Thread(() => account.withdraw(1)).start()
//  }
//  println(account.getAmount)

// OOP encapsulation is broken in a multithreaded environment
// synchronization locks to the rescue
// but even with synchronization, further problems would happen like deadlocks, livelocks etc..

  /**
   * DR #2: delegating something to a thread is a pain
   */
// you have a running thread and you want to pass a runnable to that thread
  var task: Runnable = null
  val runningThread: Thread=new Thread(()=>{
    while(true){
      while(task==null) {
        runningThread.synchronized{
          println("[background] waiting for a task")
          runningThread.wait()
        }
      }

      task.synchronized {
        println("[background] I have a task")
        task.run()
        task=null
      }
    }
  })

  def delegateToBackgroundThread(r: Runnable)={
    if(task==null) task=r
    runningThread.synchronized{
      runningThread.notify()
    }
  }

  runningThread.start()
  Thread.sleep(500)
  delegateToBackgroundThread(()=> println(42))
  Thread.sleep(1000)
  delegateToBackgroundThread(()=>println("this should run in the background"))

  /**
   * DR #3: tracing and dealing with errors in a multithreaded evn is a pin
   */
  // 1Milion numbers in between 10 threads

  println("--------------------")
  println("1 Milion")
  import scala.concurrent.ExecutionContext.Implicits.global
  val futures=(0 to 9).map(i => 100000*i until 100000*(i+1)) // 0 - 99999 , 100000 - 199999 , etc
    .map(range => Future{
      if(range.contains(546735)) throw new RuntimeException("invalid number")
      range.sum
    })

  val sumFuture=Future.reduceLeft(futures)(_ + _)   // Future with the sum of all the numbers
  sumFuture.onComplete(println)
}
