package lectures.part2oop

object Inheritance extends App {

  //single class inheritance
  class Animal {
    def eat=println("nomnom")
  }

  class Cat extends Animal

  val cat=new Cat
  cat.eat
}
