package HausAutomation

import HausAutomation.Blinds.{Blinds, GetWeatherState}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object WeatherSensor extends App {

  case object ChangeWeatherConditionPeriodically

  class WeatherSensor extends Actor {
    var currentState = "Sunny"
    var random = scala.util.Random

    override def receive: Receive = {
      case ChangeWeatherConditionPeriodically =>
        val rand = random.nextInt(2)
        if (rand == 1) {
          currentState = "Sunny"
          SendWeatherStateToBlinds()
        }
        if (rand == 0) {
          currentState = "Gloomy"
          SendWeatherStateToBlinds()
        }
        self ! ChangeWeatherConditionPeriodically
        println(s"current weather state is: $currentState")
        Thread.sleep(2000)

    }

    def SendWeatherStateToBlinds(): Unit ={
      val blinds=system.actorOf(Props[Blinds])
      blinds ! GetWeatherState(currentState)
    }
  }



  val system = ActorSystem("WeatherSensor")
  val weather = system.actorOf(Props[WeatherSensor])
  weather ! ChangeWeatherConditionPeriodically
}
