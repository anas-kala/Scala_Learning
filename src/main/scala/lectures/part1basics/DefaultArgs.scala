package lectures.part1basics

object DefaultArgs extends App {

  def trFact (n:Int,acc:Int=1):Int=   //we do not want always to pass 1 when we call this method, that is why we make it default
    if(n<=1) acc                      //this means, if I do not give you value to acc, take it on as 1
    else trFact(n-1,n*acc)

  val fact10=trFact(10,1)
  val fact5=trFact(5)

  def functionWithManyDefaultParameters(a:Int=34,b:String="Anas",c:Boolean=true)={
    println(s"I am $a years old and my name is $b and I am married = $c")
  }

  println(functionWithManyDefaultParameters());
  println(functionWithManyDefaultParameters(b="Ghiath"))
  println(functionWithManyDefaultParameters(15))
  println(functionWithManyDefaultParameters(c=true))
  //when calling functions with the names of their parameters, you can change their order
  println(functionWithManyDefaultParameters(b="Rahaf",c=false,a=30))

}
