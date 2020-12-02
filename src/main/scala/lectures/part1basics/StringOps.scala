package lectures.part1basics

object StringOps extends App {

  val str:String="Hallo, I am learning Scala"

  println(str.charAt(2))
  println(str.substring(7,11))  //7 inclusive, 11 exclusive
  println(str.split(" ").toList)
  println(str.startsWith("Hallo"))
  println(str.replace(" ","-"))
  println(str.toLowerCase())
  println(str.toUpperCase())
  println(str.length)


  val aNumberString="45"
  val aNumber=aNumberString.toInt
  println('a'+:aNumberString:+'z')
  println(str.reverse)
  println(str.take(2))

  //scala-specific: String interpolators

  //S-interpolators
  val name="David"
  val age=12
  val greeting=s"Hallo my name is $name and my age is $age"
  println(greeting)
  val anotherGreeting=s"Hallo my name is $name and my age is ${age+1}"
  println(anotherGreeting)

  //F-interpolators
  val speed=1.2f
  val myth=f"$name can eat $speed%2.2f burgers per minute"

  //raw_interpolator
  println(raw"This is a \n new line")
  val escaped="This is a \n new line"
  println(escaped)

}
