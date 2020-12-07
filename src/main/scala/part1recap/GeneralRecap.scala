package part1recap

import scala.reflect.runtime.universe.Try

object GeneralRecap extends App {
  val aCondition: Boolean = false
  //you cannot reassign to a val. You should give the type for a val
  //aCondition+=1 is not allowed

  var aVariable = 41
  //reassigning to a variable is allowed. Youd do not have to give the type to a var
  aVariable += 1

  //evaluation to a value
  val aConditionalVal = if (aCondition) 42 else 65

  //cod block
  //the result of a code block is the result of its last expression.
  //everything before the last expression maybe used to calculate this expression
  val aCodeBlock = {
    if (aCondition) 74
    56
  }

  //types
  //1. units, which are structures for doing something but return nothing. They have a side effect, like
  val theUnit = println("Hallo, scala")

  //2. functioins
  def aFunction(x: Int) = x + 1 //def functionName(parameters):return_type=function defenition
  def aFunction_two(x: Int, y: Long) = x + y

  //recursion - tail recursion
  def factorial(n: Int, acc: Int): Int =
    if (n <= 0) acc //if(n<=0) return acc
    else factorial(n - 1, acc * n)

  //OOP
  class Animal

  class Dog extends Animal

  val aDog: Animal = new Dog

  //abstract classes
  trait Carnivore {
    def eat(a: Animal): Unit //this method is unimplemented. it is abstract
  }

  class Crocodile extends Animal with Carnivore { //we have to implement everything in Animal and in Carnivore
    override def eat(a: Animal): Unit = print("crunch")
  }

  //method notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog

  //anonymous classes
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = print("roar")
  }

  aCarnivore eat aDog

  //generics
  abstract class MyList[+A] //A here is just like T, it can be any type, the pluss means, it can be a subclass of some other class

  //companion objects
  object MyList

  //case classes
  case class Person(name: String, age: Int)

  //exceptions
  val aPotentialFailure = try {
    throw new RuntimeException("I am not a failure") //Nothing
  } catch {
    case e: Exception => "I caught an exception"
      //side effects
      println("some logs")
  }

  //Functional programming
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int) = v1 + 1;
  }
  //in the following line, when the method is called like an object, the apply method
  //implemented by this function is called.
  val incremented = incrementer(42) //43

  val anonymousIncrementer = (x: Int) => x + 1
  //Int => Int === Function1[Int,Int]

  //functional programing is all about working with functions as first-class
  List(1, 2, 3).map(incrementer)
  //map here is called HOF (high order function) because it takes a function as a parameter and
  //returns another function

  //for comprehensions
  val pairs = for {
    num <- List(1, 2, 3, 4)
    char <- List('a', 'b', 'c', 'd')
  } yield num + "-" + char //this is translated as:
  //List(1,2,3,4).flatMap(num => List('a','b','c','d').map(char => num+ "-" + char))
  println("Pairs: " + pairs.toString())

  //Seq, Array, List, Vector, Map, Tupels, Sets

  //"collections"
  //Option and Try
  val anOption = Some(2)
//    val aTry= Try {
//      throw new RuntimeException
//    }
  val pairs2 = List(1, 2, 3, 4).flatMap(num => List('a', 'b', 'c', 'd').map(char => num + "_-_" + char))
  println("Pairs 2: " + pairs2.toString())

  //pattern matching
  val unknown = 1
  val order = unknown match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "unknown"
  }

  println("Order: " + order)

  val bob = Person("Bob", 22)
  val alice = Person("Alice",23)
  val greeting = alice match {
    case Person(n, _) => s"Hi, my name is $n"
    case _ => "I don't know my name"
  }
  println("greeting: " + greeting)

  //All the patterns
}
