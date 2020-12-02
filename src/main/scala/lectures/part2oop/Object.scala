package lectures.part2oop

object Object extends App {

  //SCALA DOES NOT HAVE CLASS-LEVEL FUNCTIONALITY ("static")
  //to have the same concept in Scala use objects as follows:

  //an object in scala can have values variabls and methods
  //objects do not accept parameters

  object Person{
    //static / class-level functionality
    val N_EYES=2
    def canFly: Boolean=false

    //objects in scala have factory methods
    //from here is a factory method
    //the factory method is normally called apply
    def apply(mother:Person, father:Person)=new Person("Bobbie")
  }

  class Person(val name:String){
    //instance-level functionality
  }

  //Companions is a term referring to using objects and classes within the same file
  //class Person and object Person are companions

  println(Person.N_EYES)
  println(Person.canFly)


  //differences between class-level functionality in java and objects in scala
  //scala object is a singlton instance (object person above is type + its only instance)
  val mary=Person
  val john=Person
  println(mary==john)   //results true, because the object Person is automatically singlton

  val anas=new Person("Anas")
  val hana=new Person("Hana")
  val ghiath=new Person("Ghiath")
  println(anas.equals(ghiath))

  //to use the factory method within the object Person
  val bobbie=Person.apply(hana,anas)
  //or simply
  val bob=Person(hana,anas)   //equivalent to Person.apply(hana,anas)

  //Scala Applications = Scala object with
  //def main(args: Array[String]): Unit

}
