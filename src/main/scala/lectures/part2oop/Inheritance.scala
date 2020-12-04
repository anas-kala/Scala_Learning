package lectures.part2oop

object Inheritance extends App {

  //single class inheritance
  class Animal {
    val creatureType="wild"
    protected def eat=println("nomnom")
    def drink=println("drinkdrink")   //no modifier means it is public
  }

  class Cat extends Animal{
    def crunch={
      eat
      println("crunch crunch")
    }
  }

  val cat=new Cat
//  cat.eat     //this statement is not allowed because eat is protected. which means
  //it is only visible within the inheriting class

  cat.drink

  class Person(name:String, age:Int)
  class Adult(name:String,age:Int,idCard:String) extends Person(name,age)

  class Student(name:String,age:Int){
    def this(name:String)=this(name,0)    //auxiliary constructor
  }
  //the following is also valid because the one constructor of the super accepts name with optional age.
  class CollegeStudent(name:String,age:Int) extends Student(name)

  //overriding
  class Dog extends Animal{
//    override val creatureType="domestic"
    override def drink: Unit = println("Hallo I am crunching")
  }

  val dog=new Dog
  dog.drink
  println(dog.creatureType)


  //another way of overriding a value is as follows
  class Hamster(override val creatureType: String) extends Animal{
    override def drink: Unit = super.drink
  }
  val ham=new Hamster("domestic in cage")
  println(ham.creatureType)

  //one other way of overriding a value is as follows
  class Bird(typeOfAnimal:String) extends Animal{
    override val creatureType: String = typeOfAnimal
  }
  val bird=new Bird("bird in cage")
  println(bird.creatureType)

  //type substitution
  val unknownAnimal:Animal=new Hamster("k9")    //poymorphism
  unknownAnimal.drink

  //super
  class Dolphin(typeOfAnimal:String) extends Animal{
    override val creatureType: String = typeOfAnimal

    override def drink: Unit = {
      super.drink
      println("and drinks more and more")
    }
  }

  val dolphin=new Dolphin("watery animal")
  dolphin.drink
  println(dolphin.creatureType)

  //ways to prevent inheritance
  // 1. use final keyword on members of the class to prevent them being inherited
  // 2. use final keyword on the class to prevent all its members being inherited
  // 3. use sealed keyword to enable extending the class within this file, but prevent it in other files.
}
