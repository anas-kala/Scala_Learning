package lectures.part1basics

object ValuesVariablesTypes extends App {

  val x: Int = 42 //you do not have to give the type of the val, because compiler can infer it
  val y = 50 //here the type of the value is not given.
  println(x)
  println(y)

  //vals are immutable, so you cannot assign value to them such as x=2


  val aString: String = "hello";

  val aBoolean: Boolean = false
  val aChar: Char = 'a'
  val anInt: Int = x
  val aShort: Short = 4321
  val aLong: Long = 643616182
  val aFloat: Float=2.0f
  val aDouble: Double=3.14

  //variables
  var aVariable:Int =4
  aVariable=5   //side effects, are used to show what our programs are doing


}
