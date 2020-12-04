package lectures.part2oop

object AnonymousClasses extends App {

  abstract class Animal {
    def eat: Unit
  }

  val funnyAnimal: Animal = new Animal { //anonymous class which extends class Animal
    override def eat: Unit = println("hahahahahahha")
  }
  println(funnyAnimal.getClass)

  class Person(name: String) {
    def sayHi: Unit = println(s"Hi, my name is §name, how can I help")
  }

  val jim = new Person("Jim") { //if you make an anonymous class of a class that takes parameters, you should also provide the anonymous class with the same parameters
    override def sayHi: Unit = println(s"Hi, my name is Jum, how can I be of service?")
  }

  //anonymous class work for abstract and non-abstract classes and traits.
  //if the abstracts or traits from which you make an anonymous class extend another trait or class,
  //then you should implement all the abstract members of these classes or traits.

  /*
  1. Generic trait MyPredicate[-T] with a little method test(T) => Boolean
  2. Generic trait MyTransformer[- A,B] with a method transform(A) => B
  3. MyList:
     - map(transformer) => MyList
     - filter(predicate) => MyList
     - flatMap(transformer from A to MyList[B]) => MyList[B]

     class EvenPredicate extends MyPredicate[Int]
     class StringToIntTransformer extends MyTransformer[String,Int]

     [1,2,3].map(n *2)=[2,4,6]
     [1,2,3].filter(n%2)=[2,4]
     [1,2,3].flatMap(n => [n, n+1]) => [1,2,2,3,3,4]
   */
}
