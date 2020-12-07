package lecturesAdvanced.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  /*
  the producer-consumer problem
  producer -> [x] -> consumer
   */
  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def set(newValue: Int) = value = newValue

    def get = { //getting the value will reset it to its default value.
      val result = value
      value = 0
      result
    }
  }


    def naiveProdCons()={
      val container=new SimpleContainer
      val consumer=new Thread(() => {
        println("[consumer] waiting")
        while(container.isEmpty){
          println("[consumer] actively waiting....")
        }

        println("[consumer] I have consumed "+container.get)
      })

      val producer=new Thread(() =>{
        println("[producer] computing....")
        val value=42
        println("[producer] I have produced the value "+value)
        container.set(value)
      })

      consumer.start()
      producer.start()
    }

//    naiveProdCons()

  // let's write the preceeding code using other tools wait and notify
  def smartProdCons() = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting....")
      container.synchronized {
        container.wait()
      }

      // the container must have some value
      println("[consumer] I have consumer " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] hard at work")
      Thread.sleep(2000)
      val value = 42
      container.synchronized {
        println("[consumer] I am producing " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

//  smartProdCons()

  /*
  producer -> [ ? ? ?] -> consumer

   */

  def prodConsLargeBuffer() = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer=new Thread(()=>{
      val random=new Random()

      while(true){
        buffer.synchronized{
          if(buffer.isEmpty){
            println("[consumer] buffer empty, waiting...." )
            buffer.wait()
          }

          // there must be at least one value in the buffer
          val x=buffer.dequeue()
          println("[consumer] sonsumed "+x)

          // hey consumer, there is a new value for you
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))   // for simulating waiting for the next value from the producer
      }
    })

    val prodcuer =new Thread(()=>{
      val random=new Random()
      var i=0
      while(true){
        buffer.synchronized{
          if(buffer.size==capacity) {   // buffer is full
          println("I am the [producer] the buffer is full")
          buffer.wait()
          }

          // there must be at least on empty space in the buffer to prodcue a value
          println("[producer] producing the number "+i )
          buffer.enqueue(i)

          // hey producer there is an empty space available
          buffer.notify()

          i+=1
        }
      }
    })
    consumer.start()
    prodcuer.start()
  }
//  prodConsLargeBuffer()

  /*
  producer-consumer pattern using multi producer and multi consumer
  prod-cons, level 3
  porducer1 -> [ ? ? ? ] -> consumer1
  producer2 ..............^           ^..............consumer2
   */

  class Consumer(id: Int,buffer: mutable.Queue[Int]) extends Thread{
    override def run(): Unit = {
      val random=new Random()

      while(true){
        buffer.synchronized{
          /*
          producer produces value, two consumers are waiting
          notifies one consumer, notifies one buffer
          notifies the other consumer
           */

          while(buffer.isEmpty){
            println(s"[consumer $id] buffer empty, waiting...." )
            buffer.wait()
          }

          // there must be at least one value in the buffer
          val x=buffer.dequeue()    //OOps!
          println(s"[consumer $id] sonsumed "+x)

          // hey consumer, there is a new value for you
          buffer.notify()     // notify signals one thread that they may continue
        }
        Thread.sleep(random.nextInt(500))   // for simulating waiting for the next value from the producer
      }
    }
  }

  class Producer(id: Int,buffer:mutable.Queue[Int],capacity:Int) extends Thread{
    override def run(): Unit = {
      val random=new Random()
      var i=0
      while(true){
        buffer.synchronized{
          while(buffer.size==capacity) {   // buffer is full
            println(s"I am the [producer $id] the buffer is full")
            buffer.wait()
          }

          // there must be at least on empty space in the buffer to prodcue a value
          println(s"[producer $id] producing the number "+i )
          buffer.enqueue(i)

          // hey producer there is an empty space available
          buffer.notify()
          i+=1
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiProdCons(nConsumers:Int, nProdcuer:Int)={
    val buffer:mutable.Queue[Int]=new mutable.Queue[Int]()
    val capacity=3

    (1 to nConsumers).foreach(i => new Consumer(i,buffer).start())
    (1 to nProdcuer).foreach(i => new Producer(i,buffer,capacity).start())
  }

//  multiProdCons(3,3)


  /*
  Exercise
  1) think of an example where notifyAll acts in a different way than notify?
  2) create a deadlock (a situation when one thread or many threads block each other and they cannot continue)
  3) create a livelock (a situation like deadlock when threads cannot continue but A live lock implies that threads yield execution to each other in such a way that nobody can continue.)
   */

  // notifyAll
  def testNotifyAll()={
    val bell=new Object

    (1 to 10).foreach(i => new Thread(()=>{
      bell.synchronized{
        println(s"[thread $i] waiting")
        bell.wait()
        println(s"[thread $i horray!")
      }
    }).start())

    new Thread(()=>{
      Thread.sleep(2000)
      println("[announcer] Rock and roll")
      bell.synchronized{
        bell.notifyAll()    // when substituted with bee.notify() only one thread wakes up. try it

      }
    }).start()
  }

//  testNotifyAll()

  // 2. deadlock
  case class Friend(name: String){
    def bow(other: Friend)={
      this.synchronized{
        println(s"$this: I am bowing to my friend $other")
        other.rise(this)
        println(s"$this: my friend $other has risen")
      }
    }

    def rise(other:Friend)={
      this.synchronized{
        println(s"$this: I am rising to my friend $other")
      }
    }

    var side="right"
    def switchSide()={
      if(side=="right") side="left"
      else side="right"
    }

    def pass(other:Friend)={
      while(this.side==other.side){
        println(s"$this: oh, but please, $other, feel free to pass")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }

  val sam=Friend("Sam")
  val pierre=Friend("Pierre")

//  new Thread(()=> sam.bow(pierre)).start()    // sam's lock, then pierre's lock
//  new Thread(()=>pierre.bow(sam)).start()     // pierre's lock, then sam's lock


  // 3. livelock
  new Thread(()=> sam.pass(pierre)).start()
  new Thread(()=> pierre.pass(sam)).start()
}
