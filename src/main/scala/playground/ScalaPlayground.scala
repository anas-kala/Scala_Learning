package playground

import playground.Playground.abc

object ScalaPlayground {
  def main(args: Array[String]) {
    print("Enter a number: ")
    var hours = scala.io.StdIn.readInt()
    hours = hours + 1
    println("Your entry + 1 : " + hours)
    print("Enter a greeting message: ")
    val greeting = scala.io.StdIn.readLine()
    abc(greeting)
    System.out.println("hallo World " + Math.sqrt(25))
    System.out.println("the greeting is: "+greeting)
  }
}
