package lectures.part2oop

object Generics extends App {

  class MyList[A] { //generics class
    //use the type A
  }

  trait MyListOf[A] { //generics trait
    //use the type A
  }

  class MyMyp[Key, Value] //generic class

  val listOfIntegers = new MyList[Int]
  val listOfStrings = new MyList[String]

  //generic methods
  object MyList {
    def empty[A]: MyList[A] = ???
  }

  val emptyListOfIntegers = MyList.empty[Int]

  //variance problem
  class Animal

  class Cats extends Animal

  class Dogs extends Animal

  //list[Cat] extends list[Animal] = Covariance
  class CovarianceList[+A]

  val animal: Animal = new Cats
  val animalList: CovarianceList[Animal] = new CovarianceList[Cats]

  //animalList.add(new Dog) is that allowed?  Hard Question
  //the anser: we return a list of animals
  //like this: def add[B >: A](element:B):MyList[B] = ???

  // no= invariance

  class InvariantList[A]

  //  val invariantAnimalList:InvariantList[Animal]=new InvariantList[Cats]     //is not allowed
  val invariantAnimalList: InvariantList[Animal] = new InvariantList[Animal]

  // Contravariance
  class Trainer[-A]   //contravariantlist
  val trainer:Trainer[Cats]=new Trainer[Animal]

  //bounded types
  class Cage[A <: Animal](animal: A)
  val cage=new Cage(new Dogs)

  class Car
//  val newCage=new Cage(new Car)   //is not allowed


  //the add method in the following method means:
  //if you add an object that belongs to a super class to a list of objects that belong to subclass
  //make the list a list of objects of super class type.
  class AnotherList[+A]{
    def add[B >: A](element:B):MyList[B] = ???
  }


  //expand MyList to be generic

}
