package lectures.part2oop

object OOPBasics extends App {
  val person = new Person("John", 26)
  println(person.age)
  person.greet("Daniel")
  person.greet()

  val author=new Writer("Charles","Dickens",1812)
  val imposter=new Writer("Charles","Dickens",1812)
  val novel=new Novel("Great Expectations",1861,author)

  println(novel.authorAge)
  println(novel.isWrittenBy(author))
  println(novel.isWrittenBy(imposter))

  val counter=new Counter()
  counter.inc.print     //this syntax of calling methods without parenthesis is only valid for methods with no parameters
  counter.inc.inc.inc.print
  counter.inc(10).print
}

class Person(name: String, val age: Int=0) { //constructor
  //in order to change a parameter in the person class to fields (attributes), you precede the
  //parameter with val or var.
  //body
  def greet(name: String): Unit = println(s"${this.name} says: Hi, $name")

  def greet(): Unit = println("Hi, ")

  //multiple constructors
  def this(name: String)=this(name,0)
  def this()=this("John Doe")
}

/*
Novel and Writer

Writer:first name, surname, year
-method fullname

Novel: name, year of release, author
-authorAge
-isWrittenBy(author)
-copy (new year of release)=new instance of Novel

 */
class Writer(firstName:String, sureName:String,val year:Int){
  def fullName:String=firstName+" "+sureName
}

class Novel(name:String,year:Int, author:Writer){
  def authorAge=year-author.year
  def isWrittenBy(author:Writer)=author==this.author
  def copy(newYear:Int):Novel=new Novel(name,newYear,author)
}

/*
Counter class
- receives an int value
- method current count
- method to incremetn/decrement => new Counter
- overload inc/dec to receive an amount
 */

class Counter(val count:Int=0){
  def inc={
    println("incrementing")
    new Counter(count+1)
  }    //immutability
  def dec={
    println("decremeenting")
    new Counter(count-1)
  }

  def inc(n:Int):Counter={
    if(n<=0) this
    else inc.inc(n-1)
  }

  def dec(n:Int):Counter={
    if(n<=0) this
    else dec.dec(n-1)
  }

  def print=println(count)
}



