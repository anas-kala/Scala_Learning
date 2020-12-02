package lectures.part1basics

import scala.annotation.tailrec

object Recursion extends App {
  def factorial(n: Int): Int =
    if (n <= 1) 1
    else {
      println("computing factorial of " + n + " - I first need factorial of " + (n - 1))
      var result = n * factorial(n - 1)
      println("Computed factorial of " + n)
      result
    }

  println(factorial(10))

  //but the problem with the above implementation of factorial is that it causes a stack overflow
  //when you call a big value, for example when you call factorial(5000)
  //in order to avoid the stack overflow, you should adopt another approach.
  //the following implementation shows this approach using an another variable

  def anotherFactorial(n: Int): BigInt = {
    @tailrec //add this annotation to make sure that the function you right is a tail recursive method
    def factHelper(x: Int, accumulator: BigInt): BigInt =
      if (x <= 1) accumulator
      else factHelper(x - 1, x * accumulator) //this is called "tail recursion"
    //Tail recursion= use recursive call as the last expression
    factHelper(n, 1)
  }

  /*
  This approach is called tail recursion
  why this approach of using factHelper works for big numbers?
  anotherFactorial(10)=factHelper(10,1)
  =factHelper(9,10*1)
  =factHelper(8,9*10*1)
  =factHelper(7,8*9*10*1)
  =factHelper(6,7*8*9*10*1)
  =factHelper(5,6*7*8*9*10*1)
  =factHelper(4,5*6*7*8*9*10*1)
  =factHelper(3,4*5*6*7*8*9*10*1)
  =factHelper(2,3*4*5*6*7*8*9*10*1)
  =factHelper(1,2*3*4*5*6*7*8*9*10*1) here x<=1 => accumulator=2*3*4*5*6*7*8*9*10*1=10!

  here we did not use the stack in memory, but rather simulated its behaviour but using an expression
  this is why we do not get a stack overflow
   */

  println("--------------")
  println("factorial of 5000 is:")
  println(anotherFactorial(5000))

  //when you need loops, use Tail Recursion
  //any recursive method can be turned into tail recursive by storing the the
  //intermediate results using a variable (accumulator) as done in the previous example
  //note that sometimes more than one accumulator is needed to store the intermediate results.


  /*
  do the following
  1. concatenate a string n times
  2. IsPrime function tail recursion
  3. Fibonacci function, tail recursive.
   */

  //1. concatenate a string n times
  def aGreetingFunction(name: String, age: Int) = {
    @tailrec
    def greetingHelper(nameHelper: String, ageHelper: Int, accumulator: String): String = {
      if (ageHelper <= 1) accumulator
      else
        greetingHelper(nameHelper, ageHelper - 1, accumulator + " " + name + " ")
    }

    println(greetingHelper(name, age, "Hallo "))
  }

  println("-------------")
  println("tail rec of greeting function:")
  aGreetingFunction("anas", 5)


  //2. IsPrime function using tail recursive
  def isPrimeNumber(n: Int): Boolean = {
    @tailrec
    def checkIfPrime(t: Int, accumulate: Boolean): Boolean =
      if (t <= 1) accumulate
      else
        n % t != 0 && checkIfPrime(t - 1, accumulate)

    checkIfPrime(n / 2, true)
  }

  println("-------------")
  println("tail rec of prime function of 13:")
  println(isPrimeNumber(13))
  println("tail rec of prime function of 15:")
  println(isPrimeNumber(15))
  println("tail rec of prime function of 15:")
  println(isPrimeNumber(43))


  //3. Fibonacci function, tail recursive.
  def fibonacciFunction(n: Int): Int = {
    @tailrec
    def helperFibonacci(x: Int, last: Int, nextToLast: Int): Int = {
      if (x >= n) last
      else helperFibonacci(x + 1, last + nextToLast, last)
    }
    if (n <= 2) 1
    else helperFibonacci(2, 1, 1)
  }

  println("-------------")
  println("tail rec of fibonaci function of 8:")
  println(fibonacciFunction(8))
}
