package part1recap

import scala.concurrent.Future

object AdvancedRecap extends App {

  // partial functions
  // the partial function only accepts specified parameters and returns specified
  // values. otherwise it throws an exception
  val partialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 65
    case 5 => 999
  }
  // the equivalent function the above-mentioned partial-function is:
  val pf = (x: Int) => x match {
    case 1 => 42
    case 2 => 65
    case 5 => 999
  }

  val function: (Int => Int) = partialFunction

  val modifiedList = List(1, 2, 3).map {
    case 1 => 42
    case _ => 0
  }

  // lifting
  val lifted = partialFunction.lift //total function Int => Option[Int]. lifting turns a partial function into a total function
  lifted(2) // Some(65)
  lifted(500) // None
  println(lifted(2) + "  " + lifted(3) + "  " + lifted(500))

  // orElse   which extends a partial function into another partial function
  val pfChain = partialFunction.orElse[Int, Int] {
    case 60 => 9000
  }

  pfChain(5) //999
  pfChain(60) //9000
  //  pfChain(456)//throw a MatchError

  // type aliases
  type ReceiveFunction = PartialFunction[Any, Unit] // here ReceiveFunction is an ilias to PartialFunction[Any,Unit]
  // for example
  def receive: ReceiveFunction = {
    case 1 => println("hellow")
    case _ => println("confused....")
    case 4 => 54
  }

  println(receive("sdfa"))

  // implicits
  implicit val timout = 3000

  def setTimeout(f: () => Unit)(implicit timeout: Int) = f() //the method setTimeout here receives two parameters
  //these parameters are (f:()=> Unit) and (implicit timeout:Int). when calling setTiemout, we can relenquish the
  //second parameter because it has been declared implicit with a value of 3000. Example
  setTimeout(() => println("Hallo"))
  //of
  setTimeout(() => (3 + 4))(5000) //here timeout has is given a value other than the default


  // implicit conversions
  // 1. implicit defs
  case class Person(name :String){
    def great=s"Hi, my name is $name"
    def writeMyName=println("My name is: "+this.name)
  }

  implicit def fromStringToPerson(string:String):Person=Person(string)
  //instead of doing the call: fromStringToPerson("Peter").greet, you can do the following:
  "peter".great
  //instead of fromStringToPerson("Anas").writeMyName
  "Anas".writeMyName

  // 2. implicit classes
  implicit class Dog(name: String){
    def bark=println("bark")
  }
  //instead of calling: new Dog("Lassie").bark
  "Lassie".bark

  // Note: you should pay attention to how this is organized
  // local scope (which in our case happens to be the whole body of the class
  implicit val inverseOrder:Ordering[Int] =Ordering.fromLessThan(_>_)
  // the implementation of the sorted method, takes a second implicit parameter of type Ordering[Int]
  List(1,2,3).sorted  //List(3,2,1)


  // imorted scope
  import scala.concurrent.ExecutionContext.Implicits.global   //global here is an implicit value, which will be
  val future=Future{                                          //automatically injected by the compiler into the
    println("Hello, future")                                  //apply method of Future
  }

  // companion objects of the types included in the call
  object Person{
    implicit val personOrdering: Ordering[Person]=Ordering.fromLessThan((a,b)=>a.name.compareTo(b.name)<0)
  }

  List(Person("Bob"),Person("Alice")).sorted    // in this case the compiler will inject the implicit value
                                                //which is of type Ordering[Person] from the companion object, and
                                                //will inject it directly in the sorted method.
  // the return value in this case is: List(Person("Alice"),Person("Bob"))
}
