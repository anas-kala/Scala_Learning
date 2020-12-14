package HausAutomation

import HausAutomation.WeatherSensor.WeatherSensor
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object Blinds extends App {

  case class GetWeatherState(state: String)

  case class GetMediaStationState(mss: String)

  case object GetBlindState

  class Blinds extends Actor {
    var currentWeatherState = ""
    var blindState = ""
    var mediaStation = ""

    override def receive: Receive = {
      case GetMediaStationState(mss) =>
        mediaStation = mss
        println(mediaStation + "------------")
      case GetWeatherState(state) =>
        currentWeatherState = state
        println(currentWeatherState + "............")
      case GetBlindState =>
        if (currentWeatherState.equals("Sunny")) { // wenn es sonnig ist, die blinds sind sowieso zu.
          blindState = "closed"
          println(s"blind state is1: $blindState ")
        }
        if (currentWeatherState.equals("Gloomy") && mediaStation.equals("plying")) {
          blindState = "closed"
          println(s"blind state is2: $blindState ")
        }
        if (currentWeatherState.equals("Gloomy") && mediaStation.equals("stopped")) {
          blindState = "opened"
          println(s"blind state is3: $blindState ")
        }
    }
  }

  val system = ActorSystem("blinds")
  val blind = system.actorOf(Props[Blinds])
  blind ! GetMediaStationState("stopped")
  blind ! GetWeatherState("Sunny")
  blind ! GetBlindState
}
