package HausAutomation

import HausAutomation.Fridge.Fridge.{ConsumeFromFridge, PutIntoFridge, PutIntoFridgeGroup, QueryContents}
import akka.actor.{Actor, ActorSystem, Props}

import java.time.LocalDate
import scala.util.control.Breaks.{break, breakable}
import scala.util.control.ControlThrowable

object Fridge extends App {
  val weightCapacity = 1000
  val spaceCapacity = 200
  var currentWeightOfContents = 0
  var currentSpaceOfContents = 0
  var contents: Set[Product] = Set()
  var contentsTypes = List[String]("apple", "orange", "watermelon", "kiwi", "meet") // this fridge is designed to contain five different types of products. orange, apple, watermelon, kiwi, meet
  var history: Map[LocalDate, Product] = Map()

  object Fridge {

    case class ConsumeFromFridge(name: String)

    case class PutIntoFridge(product: Product)

    case class PutIntoFridgeGroup(product: Product, number: Int)

    case class MakeOrder(product: Product)

    case class MakeCustomizedOrder(product: Product, weight: Int, space: Int)

    case class QueryContents(productName: String)

  }

  class Fridge extends Actor {

    import Fridge._

    override def receive: Receive = {
      case ConsumeFromFridge(name) => // when a product runs out from the fridge, it gets automatically ordered.
        val pr = consumingProduct(name)
        if (pr == 0) {
          val product = new Product()
          product.setName(name)
          self ! MakeOrder(product)
        }

      case PutIntoFridge(product) =>
        if (currentSpaceOfContents + product.getSpace <= spaceCapacity && currentWeightOfContents + product.getWeight <= weightCapacity) {
          currentWeightOfContents += currentWeightOfContents + product.getWeight();
          currentSpaceOfContents += currentSpaceOfContents + product.getSpace();
          contents = contents + product
        }

      case PutIntoFridgeGroup(product, number) =>
        (1 to number).foreach(_ => {
          val prod = new Product()
          prod.setName(product.getName)
          prod.setPrice(product.getPrice)
          prod.setSpace(product.getSpace)
          prod.setWeight(product.getWeight)
          self ! PutIntoFridge(prod)
        })

      case MakeOrder(product) =>
        if (product.getName().equals("meet"))
          tryOrdering(product, 5);
        else if (product.getName().equals("apple"))
          tryOrdering(product, 10);
        else if (product.getName().equals("orange"))
          tryOrdering(product, 10);
        else if (product.getName().equals("watermelon"))
          tryOrdering(product, 1);
        else if (product.getName().equals("kiwi"))
          tryOrdering(product, 10);

      case MakeCustomizedOrder(product, weight, space) =>
        if (enoughSpace(space) && enoughtWeight(weight)) {
          println(weight + " kilos of " + product.getName() + " has been ordered");
          currentSpaceOfContents -= space; // reserving space for the order
          currentWeightOfContents -= weight; // reserving weight for the order
        }

      case QueryContents(productName) =>
        if (productName != "watermelon" && productName != "orange" && productName != "kiwi" && productName != "apple" && productName != "meet")
          println("the product you entered does not belong to the contents of the fridge");
        if (productName.equals("orange"))
          DisplayProductInformation("orange")
        else if (productName.equals("watermelon"))
          DisplayProductInformation("watermelon")
        else if (productName.equals("kiwi"))
          DisplayProductInformation("kiwi")
        else if (productName.equals("apple"))
          DisplayProductInformation("apple")
        else if (productName.equals("apple"))
          DisplayProductInformation("apple")
        else if (productName.equals("meet"))
          DisplayProductInformation("meet")

    }


  }

  def consumingProduct(name: String): Int = {
    var productsWithName: Set[Product] = Set()
    contents.foreach(x =>
      if (x.getName.equalsIgnoreCase(name))
        productsWithName = productsWithName + x
    )
    if (!productsWithName.isEmpty) {
      contents = contents - productsWithName.head // remove the consumed product from the fridge.
      currentSpaceOfContents -= productsWithName.head.getSpace
      currentWeightOfContents -= productsWithName.head.getWeight
    }
    if (productsWithName.size <= 2) // if the fridge contains less than 2 of the product, return 0 so that the caller method makes an automatic order.
      0
    else
      productsWithName.size
  }

  def tryOrdering(product: Product, a: Int): Unit = {
     if (enoughSpace(a) && enoughtWeight(a * 5)) {
        System.out.println(a * 5 + " kilos of " + product.getName() + " have been ordered")
        currentSpaceOfContents -= a // reserving space for the order
        currentWeightOfContents -= a * 5 // reserving weight for the order
        // for the history
        val date: LocalDate = LocalDate.now()
        history = history + (date -> product)
       // after 1 second, the order arrives and is placed in the fridge
       Thread.sleep(1000)
       for(_ <- 1 to a){
         PutIntoFridge(product)
       }
      }
  }

  def enoughSpace(spaceOfOrder: Int) = {
    if (spaceOfOrder + currentSpaceOfContents < spaceCapacity)
      true
    else
      false
  }

  def enoughtWeight(weightOfOrder: Int) = {
    if (weightOfOrder + currentWeightOfContents < weightCapacity)
      true
    else
      false
  }

  def DisplayProductInformation(str: String): Unit = {
    var count = 0
    var space = 0
    var weight = 0
    contents.foreach(e =>
      if (e.getName().equals(str)) {
        count += 1
        space += e.getSpace()
        weight += e.getWeight()
      })
    println("Query results: of the product " + str + " you still have " + count + " pieces. The space used for that is: " + space + " and weight used: " + weight);
  }

  val system = ActorSystem("Fridge")
  val fridge = system.actorOf(Props[Fridge])
  fridge ! PutIntoFridge(new Product("apple", 1, 1, 5))
  fridge ! PutIntoFridge(new Product("apple", 1, 1, 5))
  fridge ! PutIntoFridge(new Product("meet", 5, 5, 20))
  fridge ! PutIntoFridge(new Product("kiwi", 1, 1, 5))
  fridge ! PutIntoFridge(new Product("kiwi", 1, 1, 5))
  fridge ! PutIntoFridge(new Product("orange", 1, 1, 5))
  fridge ! PutIntoFridge(new Product("watermelon", 1, 1, 5))


  fridge ! PutIntoFridgeGroup(new Product("watermelon", 1, 1, 5), 6)
  fridge ! PutIntoFridgeGroup(new Product("meet", 1, 1, 5), 6)
//  Thread.sleep(1000)
  fridge ! ConsumeFromFridge("kiwi")
  fridge ! ConsumeFromFridge("kiwi")
  fridge ! QueryContents("kiwi")
  fridge ! QueryContents("apple")
  fridge ! QueryContents("orange")
  fridge ! QueryContents("meet")
  fridge ! QueryContents("watermelon")
  fridge ! ConsumeFromFridge("orange")


}
