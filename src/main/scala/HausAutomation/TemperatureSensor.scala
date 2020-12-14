package HausAutomation

import akka.actor.{Actor, ActorSystem, Props}

import scala.util.Random

object TemperatureSensor extends App {
  case object ChangeTempPeriodically

  val system = ActorSystem("TemperatureSensor")

  class TemperatureSensor extends Actor {
    var temp = 23f
    var random = scala.util.Random

    override def receive: Receive = {
      case ChangeTempPeriodically =>
        Thread.sleep(1000)
        temp +=(random.nextFloat())
        if (temp < 20) {
          // todo send message to AC to turn off
          println(s"current Temperature is: $temp, AC is off")
          Thread.sleep(500)
          temp +=(random.nextFloat())
          self ! ChangeTempPeriodically
        }
        if (temp > 20 && temp<30) {
          // todo send message to AC to start cooling
          println(s"current Temperature is: $temp, AC is on cooling")
          Thread.sleep(500)
          temp -=(random.nextFloat())
          self ! ChangeTempPeriodically
        }
    }
  }

  val temp=system.actorOf(Props[TemperatureSensor])
  temp ! ChangeTempPeriodically
}

