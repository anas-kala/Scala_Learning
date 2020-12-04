package lectures.part2oop

object CaseClasses extends App {

  /*
  equals, hashcode, toString
   */

  case class Person(name:String,age:Int)

  // 1. class parameters are fields, when the class is proceeded with case
  val jim =new Person("Jim",34)
  println(jim.name+" , "+jim.age)

  // 2.sensible toString
  // in scala println(instance) = println(instance.toString)  which is another syntactic sugar
  println(jim.toString)
  println(jim)

  // 3. equals and hashcode implemented OOTB
  val jim2=new Person("jim",34)
  println(jim==jim2)    //returns true due to the face that equals compares objects according to their attributes

  // 4. case classes have handy copy method
  val jim3=jim.copy(age=45)     //new compy (not shallow copy of jim) with age = 45
  println(jim3)

  // 5. case classes have companion objects
  val thePerson = Person
  //companions have a factory method
  val mary=Person("Mary",23)      //apply method

  // 6. case classes are serializable
  // Akka framework (sending objects in the net)

  // 7. case classes extractor patterns = case classes can be used in pattern matching

  case object UnitedKingdom{    //this is a case object. Case objects have the same property as the case classes but they do not have a companion object, because they are their own companion object
    def name:String = "The Uk of GB and NI"
  }


  /*
  Expand MyList to use case classes and case objects
   */
}
