package lectures.part3functionalprogramming

object WhatsAFunction extends App {

  // Target: use functions as first class elements
  // problem: oop

  val doubler = new MyFunction[Int, Int] {
    override def apply(element: Int): Int = element * 2
  }

  println(doubler(2))

  //function types = Function1[A,B]
  val stringToIntConverter = new Function[String, Int] {
    override def apply(string: String): Int = string.toInt
  }

  println(stringToIntConverter("3") + 4)

  val adder: ((Int, Int) => Int) = (a: Int, b: Int) => a + b

  // Function types Function2[A, B, R] === (A,B) => R

  // All SCALA Functions are objects

  /*
  1. a function which takes 2 string and concatinates them
  2. transform the MyPreidcate and MyTransformer into function types
  3. define a function which takes an int and returns another function which takesw an int and returns an int
      - what's the type of this function
      - how to do it
   */

  def concatenator: (String, String) => String= new Function2[String,String,String]{
    override def apply(a: String,b :String):String=a+b
  }

  println(concatenator("Hallo","Scala"))
}

trait MyFunction[A, B] {
  def apply(element: A): B
}