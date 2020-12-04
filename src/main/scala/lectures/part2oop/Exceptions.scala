package lectures.part2oop

object Exceptions extends App {

  //  val x:String =null
  //  println(x.length)

  // 1. throwing and catching exceptions
  //  val aWeirdValue:String=throw new NullPointerException

  // throwable classes extend the Throwable class.
  // Exception and Error are the major Throwable subtypes.

  // 2. how to catch exception
  def getInt(withException: Boolean): Int =
    if (withException) throw new RuntimeException("No Int for your")
    else 42

  val potentialFail = try {
    // code that might fail
    getInt(true)
  } catch {
    case e: RuntimeException => println("caught a Runtime exception")
  } finally {
    //finally is optional and does not influence the return type of this expression
    //use finally only for side effects
    // code that will get executed no matter what
    println("finally")
  }

  // 3. how to define your own exceptions?
  class MyException extends Exception

  val exceptions = new MyException

  //  throw exceptions

  /*
  1. crash your program with an OutOfMemoryError
  2. crash with SOError stack overflow error
  3. pocketCalculator
      - add(x,y)
      - multiply(x,y)
      - divide(x,y)
      - subtract(x,y)

      Throw
        - OverflowException if add(x,y) exceeds Int.MAX_VALUE
        - UnderflowException if subtract(x,y) exceeds Int.MIN_VALUE
        - MathCalculationException for division by 0
   */

  //  val array=Array.ofDim(Int.MaxValue)   //this crashes the program with OutOfMemoryError

  //  def infinit:Int=1+infinit     //this will crash the program with stack overflow
  //  val noLimit=infinit

  class OverflowException extends Exception

  class UnderflowEsception extends Exception

  class MathCalculationException extends Exception("Division by zero")

  object PocketCalculator {
    def add(x: Int, y: Int) = {
      val result = x + y
      if (x > 0 && y > 0 && result < 0) throw new OverflowException
      else if (x < 0 && y < 0 && x + y > 0) throw new UnderflowEsception
      else result
    }

    def subtract(x: Int, y: Int): Int = {
      val result = x - y
      if (x > 0 && y < 0 && result < 0) throw new OverflowException
      else if (x < 0 && y > 0 && result > 0) throw new UnderflowEsception
      else result
    }

    def multiply(x: Int, y: Int): Int = {
      val result = x * y
      if (x > 0 && y > 0 && result < 0) throw new OverflowException
      else if (x < 0 && y < 0 && result > 0) throw new UnderflowEsception
      else if (x > 0 && y < 0 && result > 0) throw new UnderflowEsception
      else result
    }

    def divide(x: Int, y: Int): Int = {
      if (y == 0) throw new MathCalculationException
      else x / y
    }
  }

  //  println(PocketCalculator.add(Int.MaxValue,10))
  println(PocketCalculator.divide(2, 0))
}
