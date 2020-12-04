package lectures.part2oop

object AbstractDataTypes extends App {

  // abstract
  abstract class Animal {
    val creatureType: String //by not initializing this field it is automatically abstract
    def eat: Unit //by not giving the definition of this method it is automatically abstract
  }

  //  val animal=new Animal         //you cannot instantiate an abstract class

  class Dog extends Animal {
    override val creatureType: String = "Canine" //override keyword is not mandatory for overriding abstract memebrs, so you can here do with out it

    override def eat: Unit = println("crunch crunch") //override keyword is not mandatory for overriding abstract memebrs, so you can here do with out it
  }

  //traits   which are the ultimate abstract data types in scala
  //traits are like abstract classes, they have abstract members

  trait Carnivore {
    def eat(animal: Animal): Unit //this method is abstract
    val preferredMeal:String="fresh meat"
  }


  trait ColdBlooded

  class Crocodile extends Animal with Carnivore with ColdBlooded{
    override val creatureType: String = "croc"

    override def eat: Unit = println("nomnomnom")

    override def eat(animal: Animal): Unit = println(s"I am a croc and I am eating ${animal.creatureType}")
  }

  val dog = new Dog
  val croc = new Crocodile
  croc.eat(dog)

  //difference between abstract classes and traits
  //abstract classes and traits can have abstract and non-abstract members
  //1. traints do not have constructor parameters
  //2. you can only extend one abstract class but as many traits as you want (multiple inheritance)
  //3. traits = behaviour, abstract class ="thing"  (same difference between abstract class and interface)



}
