package HausAutomation

import akka.actor.Actor

import java.time.LocalDate
import scala.util.control.Breaks.break

object Fridge extends App {
  val weightCapacity = 1000
  val spaceCapacity = 200
  var currentWeightOfContents = 0
  var currentSpaceOfContents = 0
  var contents: Set[Product] = Set()
  var contentsTypes = List[String]("apple", "orange", "watermelon", "kiwi", "meet") // this fridge is designed to contain five different types of products. orange, apple, watermelon, kiwi, meet
  var history: Map[LocalDate, Product] = Map()

  object Fridge {

    //    case object GetWeightCapacity
    //    case object GetSpaceCapacity
    case class ConsumeFromFridge(product: Product)

    case class PutIntoFridge(product: Product)

    case class MakeOrder(product: Product)

    case class MakeCustomizedOrder(product: Product, weight: Int, space: Int)

    case class QueryContents(productName: String)
  }

  class Fridge extends Actor {

    import Fridge._

    override def receive: Receive = {
      case ConsumeFromFridge(product) => // when a product runs out from the fridge, it gets automatically ordered.
        currentSpaceOfContents -= product.getSpace
        currentWeightOfContents -= product.getWeight
        contents.foreach(_ => {
          if (!contentsTypes.contains(product.getName))
            MakeOrder(product)
        })

      case PutIntoFridge(product) =>
        if (currentSpaceOfContents + product.getSpace <= spaceCapacity && currentWeightOfContents + product.getWeight <= weightCapacity) {
          contents = contents + product
          currentWeightOfContents += currentWeightOfContents + product.getWeight();
          currentSpaceOfContents += currentSpaceOfContents + product.getSpace();
        }

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
    }


  }

  def tryOrdering(product: Product, a: Int): Unit = {
    for (i <- a to 1) {
      if (enoughSpace(i) && enoughtWeight(i * 5)) {
        System.out.println(i * 5 + " kilos of " + product.getName() + " has been ordered")
        currentSpaceOfContents -= i // reserving space for the order
        currentWeightOfContents -= i * 5 // reserving weight for the order
        // for the history
        val date: LocalDate = LocalDate.now()
        history = history + (date -> product)
        break;
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
    var count = 0;
    var space = 0;
    var weight = 0;
    contents.foreach(e =>
      if (e.getName().equals(str)) {
        count += 1;
        space += e.getSpace();
        weight = e.getWeight();
      })
    println("of the product " + str + " you still have " + count + ". space used: " + space + ", weight used: " + weight);
  }
}
