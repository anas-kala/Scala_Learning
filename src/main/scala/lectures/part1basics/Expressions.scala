package lectures.part1basics


object Expressions extends App {
  val x = 1 + 2 //expressions are evaluated to a value
  println(x)

  println(2 + 3 * 4)

  //changing a variable is called in scall a "side effect"
  //the following operations are all side effects
  //-=, +=, /=, ..... are all side effects


  //instructions vs expressions
  //instruction is something you tell the computer to do
  //expression is something that has a value or/and type

  //if expression in scala
  val aCondition = true
  val aConditionValue = if (aCondition) 5 else 3 //5 if true, 3 if false
  println(aConditionValue)

  //in scala never write this. while loops are discouraged in scala. they are only good
  //in imperative languages like java, c#...
  //scala is not imperative.
  var i = 0
  val aWhile = while (i < 10) { //Unit === void
    println(i)
    i += 1
  }

  //everything in scala is expression
  //side effects in scala are expressions that return Unit
  println(aWhile)

  //side effects: println(), whiles, reassigning


  //cod blocks
  //everything within the code block is seen only within the code block
  val aCodeBlock = { //code blocks are expressions. the value of code block is the value of its last line
    val y = 2
    val z = y + 1
    if (z > 2) "hello" else "good bye"
  }

  //1. what is the difference between "hello world" vs println("hello world")
  //"hello world" is a String, println("hello world") is an expression, which is a side effect (expression returning Unit)

  //2. what is the vlaue of
  val someValue = { //Boolean
    2 < 3
  }
  println(someValue)

  //3. what is the value of
  val someOtherValue = { //42
    if (someValue) 239 else 986
    42
  }
  println(someOtherValue)
}
