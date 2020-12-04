package excercise

import scala.collection.BitSet.empty.head
import scala.collection.immutable.Stream
import scala.collection.immutable.Stream.Empty.tail

abstract class MyListGenerics[+A] {

  /*
  head = first element of the list
  tail = remainder of the list
  isEmpty = is this list empty
  add(int) => new list with this element added
  toString => a string representation of the list
   */

  def head: A

  def tail: MyListGenerics[A]

  def isEmpty: Boolean

  def add[B >: A](element: B): MyListGenerics[B] //immutable list because we are not modifying it but rather return a new instance when we want to modify it

  def printElements: String

  override def toString: String  = "[" + printElements + "]"
}

object Empty1 extends MyListGenerics[Nothing] {
  override def head: Nothing = throw new NoSuchElementException

  override def tail: MyListGenerics[Nothing] = throw new NoSuchElementException

  override def isEmpty: Boolean = true

  override def add[B>:Nothing](element: B): MyListGenerics[B] = new Cons1(element, Empty1)

  override def printElements: String = ""
}

class Cons1[+A](h: A, t: MyListGenerics[A]) extends MyListGenerics[A] {
  override def head: A = h

  override def tail: MyListGenerics[A] = t

  override def isEmpty: Boolean = false

  override def add[B>:A](element: B): MyListGenerics[B] = new Cons1(element, this)

  override def printElements: String = {
    if (t.isEmpty) "" + h
    else h + " " + t.printElements
  }
}

object ListTest1 extends App {
  val listOfIntegers:MyListGenerics[Int]=new Cons1(1,new Cons1(2,new Cons1(3,Empty1)))
  val listOfStrings:MyListGenerics[String]=new Cons1("Hallow",new Cons1("Scala",Empty1))
  println(listOfIntegers.toString)
  println(listOfStrings.toString)
}

