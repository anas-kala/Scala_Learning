package excercise

import scala.collection.BitSet.empty.head
import scala.collection.immutable.Stream
import scala.collection.immutable.Stream.Empty.tail

abstract class MyList {

  /*
  head = first element of the list
  tail = remainder of the list
  isEmpty = is this list empty
  add(int) => new list with this element added
  toString => a string representation of the list
   */

  def head: Int

  def tail: MyList

  def isEmpty: Boolean

  def add(element: Int): MyList //immutable list because we are not modifying it but rather return a new instance when we want to modify it

  def printElements: String

  override def toString: String  = "[" + printElements + "]"
}

object Empty extends MyList {
  override def head: Int = throw new NoSuchElementException

  override def tail: MyList = throw new NoSuchElementException

  override def isEmpty: Boolean = true

  override def add(element: Int): MyList = new Cons(element, Empty)

  override def printElements: String = ""
}

class Cons(h: Int, t: MyList) extends MyList {
  override def head: Int = h

  override def tail: MyList = t

  override def isEmpty: Boolean = false

  override def add(element: Int): MyList = new Cons(element, this)

  override def printElements: String = {
    if (t.isEmpty) "" + h
    else h + " " + t.printElements
  }
}

object ListTest extends App {
  val list = new Cons(1, new Cons(2, new Cons(3, Empty)))
  println(list.head)
  println(list.add(4).head)
  println(list.isEmpty)
  //polymorphic call
  println(list.toString)
}

