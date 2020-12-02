package lectures.part1basics

object Functions extends App {

  def aFunction(a:String,b:Int)= {   //the compiler can infer the return type, you do not have to give it.
    a+" "+b
  }

  println(aFunction("hello",3))

  def aParameterlessFunction():Int=42
  println(aParameterlessFunction())
  println(aParameterlessFunction)   //a parameterless function can be called only by its name

  //in scala you should use recursive functions instead of loops, like the following
  //when you need loops, use recursive functions
  //note: for recursive functions, you should explicitly give the return type, unlike normal functions.
  def aRepeatedFunction(aString:String,n:Int):String={
    if(n==1) aString
    else aString +aRepeatedFunction(aString,n-1)
  }

  println(aRepeatedFunction("hello\n",3))

  def aFunctionWithSideEffects(aString:String)=print(aString)

  def aBigFunction(n:Int)={
    def aSmallerFunction(a: Int,b:Int)=a+b
    aSmallerFunction(n,n-1)
  }

  /*
  write the following function
  1. A greeting function (name,age) => "Hi, my name is $name and I am $age years old"
  2. factorial function of n
  3. fibonacci function
  4. function for testing if a number is prime
   */

  def aGreetingFunction(name:String,age:Int)={
    println(s"Hi, my name is $name and I am $age years old")
  }
  println(aGreetingFunction("David",12))

  def aFactorialFunction(n:Int):Int={
    if(n==1) 1
    else
      n*aFactorialFunction(n-1)
  }
  println(aFactorialFunction(5))

  def aFibonacciFunction(n:Int):Int={
    if(n<=1) 1
    else aFibonacciFunction(n-1)+aFibonacciFunction(n-2)
  }
  println(aFibonacciFunction(8))

  def isPrime(n:Int):Boolean={
    def isPrimeUntil(t:Int):Boolean=
      if(t<=1) true
      else
        n% t!=0 && isPrimeUntil(t-1)
      isPrimeUntil(n/2)
  }

  println(isPrime(37))
  println(isPrime(2003))
  println(isPrime(37*17))
}
