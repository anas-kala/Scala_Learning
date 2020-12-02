package lectures.part2oop

object Notaion extends App {

  class Person(val name: String, favoriteMovie: String,val age: Int = 0) {
    def likes(movie: String): Boolean = movie == favoriteMovie

    def hangOutWith(person: Person): String = s"${this.name} is hanging with ${person.name}"

    def +(person: Person): String = s"${this.name} is now with ${person.name}"

    def +(nickname: String): Person = new Person(s"$name ($nickname)", favoriteMovie)

    def unary_! : String = s"$name what the heck"

    def unary_+ : Person = new Person(name, favoriteMovie, age + 1)

    def isAlive: Boolean = true

    def apply(): String = s"Hi, my name is $name and I like $favoriteMovie"

    def apply(n:Int):String=s"$name watched $favoriteMovie $n times"

    def learns(thing:String) =s"$name is learning $thing"

    def learnsScala=this learns "Scala"
  }

  val mary = new Person("Mary", "Inception")
  println(mary.likes("Inception"))
  //an equivalent statement would by:
  println(mary likes "Inception")
  //this is called infix notation = operator notation = syntactic sugar
  //it is valid for methods with one parameter


  //"Operators" in Scala
  val tom = new Person("Tom", "Fight Club")
  println(mary hangOutWith tom)

  //you can even give + or / or - or * or # or $ or .... as a name of the method as in this example
  val anas = new Person("Anas", "Premonition")
  println(anas + tom)
  println(anas.+(tom))
  println(1.+(2))

  //Important Note to take away: All operators are methods in scala

  //prefix notation (another form of syntactic sugar)
  val x = -1 //is equivalent to 1.unary_-
  val y = 1.unary_-
  //unary_ prefix only works with - + * ~ !

  //the following two statements are equivalent
  println(!mary)
  println(mary.unary_!)

  //postfix notation
  //only methods that do not have any parameter can be used with the postfix notation
  //the following two statements are equivalent
  println(mary.isAlive)
  println(mary isAlive)

  //the special method apply
  //the following two statements are equal
  //whenever the compiler sees that an object is being called as a mthod(because it has () if font of it)
  //it knows that the class from which this object has been instantiated implements the method apply()
  //and calls it.
  //object() => calls the apply() method implemented in the class from which object is instantiated
  println(mary.apply())
  println(mary())

  /*
  1. override the + operator
      mary - "the rockstar" => new person "Mary (the rockstar)"

  2. add an age to the Person class
      add a unary + operator => new person with the age +1
      +mary => mary with the age incrementer

  3. add a "learns" method in the Person class => Mary learns Scala
      add learnScala method with no parameters, calls learns method with "Scala".
      Use it in postfix notation

   4. overload the apply method
      mary.apply(2) => "Mary watched Inception 2 times"
   */

  println((mary + "the rock star") ()) //apply()
  //or equivalently
  println((mary + "the rock star").apply())


  println((+mary).age)
  println(mary learnsScala)
  println(mary(10))   //calls apply method with the parameter n=10
}
