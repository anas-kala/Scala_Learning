package lectures.part1basics

object CallFunctionByNameVSCallFunctionByValue extends App {

  def calledByValue(x: Long): Unit = {
    println("by value: " + x)
    println("by value: " + x)
  }

  def calledByName(x: => Long): Unit = {
    println("by name: " + x)
    println("by name: " + x)
  }

  //calling by value will first evaluate the parameters and then pass then to
  //the method calledByValue
  //i.e. System.nanoTime()=612393960805200 is passed to calledByValue
  calledByValue(System.nanoTime())

  //calling by name will pass the parameter as it is
  //i.e. System.nanoTime() is passed to the body of the method calledByName
  //and the evaluation will take place within the body of this method.
  calledByName(System.nanoTime())

  def infinite():Int=1+infinite()   //this call would cause an overflow because it is infinit loop
  def printFirst(x:Int,y: => Int)=println(x)    //here we have calling by name (=>), which means that its evaluation will be delayed untill it is needed.

  //the following call would only evaluate x according to the way printFirst is defined.
  //this means, the following call will not cause any overflow because infinit() as the second parameter
  //is not going to be evaluated since it is called by name.
  printFirst(34,infinite())

  //try changing the call by name in the method printFirst() by removing the "=>" to see the difference.
}
