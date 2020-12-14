package lecturesAdvanced.part3concurrency

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference
import scala.collection.parallel.{ForkJoinTaskSupport, Task}
import scala.collection.parallel.immutable.ParVector

object ParallelUtils extends App {

  // 1. parallel collections
  // we create a parallel collection by .par. a parallel collection means that operations on it are handled by multiple threads
  val parList=List(1,2,3).par
  val aParVector=ParVector[Int](1,2,3)    // parallel vectors

  /*
  seq
  Vector
  Array
  Map - Hash, Trie
  Set -Hash, Trie
   */

  def measure[T](operation: => T): Long={
    val time=System.currentTimeMillis()
    operation
    System.currentTimeMillis()-time
  }

  val list=(1 to 10000).toList
  val serialTime=measure{
    list.map(_ +1)
  }
  println("serial time: "+serialTime)

  val paralletlTime=measure{
    list.map(_ +1)
  }
  println("parallel time: "+ paralletlTime)

  /*
  Map-reduce model
  - split the elements into chunks - Splitter
  - operation
  - recombile - combiner
   */

  // map, flatMap, filter, foreach, reduce, fold

  // fold, reduce with non-associative operators
  println(List(1,2,3).reduce(_-_))
  println(List(1,2,3).par.reduce(_-_))

  // synchronization
  var sum=0
  List(1,2,3).par.foreach(sum+= _)
  println(sum)    // race conditions!

  // configuring
  aParVector.tasksupport=new ForkJoinTaskSupport(new ForkJoinPool(2))
  /*
  alternatives
  - ThreadPoolTaskSupport -deprecated
  - ExecutionContextTaskSupport(EC)
   */

  aParVector.tasksupport=new ForkJoinTaskSupport {

    override def execute[R, Tp](task: Task[R, Tp]): () => R = super.execute(task)

    override def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = super.executeAndWaitResult(task)

    override def parallelismLevel: Int = super.parallelismLevel

    override val environment: ForkJoinPool = ???
  }

  // 2 - atomic ops and references
  val atomic =new AtomicReference[Int](2)   // they have atomic operations

  val currentValue=atomic.get()     // thread-safe read
  atomic.set(4)   // thread-safe write

  atomic.getAndSet(5)     // thread safe combo

  atomic.compareAndSet(38,56)   // if the value is 38, then set to 56. thread-safe operation

  atomic.updateAndGet(_+1)  // thread-safe function run
  atomic.getAndUpdate(_+1)

  atomic.accumulateAndGet(12,_+_)   // thread-safe accumulation
  atomic.getAndAccumulate(12,_+_)
}
