package HausAutomation

import HausAutomation.Blinds.{Blinds, GetMediaStationState}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object MediaStation extends App {

  case class PlayMovie(movieName: String)

  case object StopCurrentMovie
  case object GetState

  class MediaStation extends Actor {
    var current = "stopped"
    var name = ""

    override def receive: Receive = {
      case PlayMovie(movieName) =>
        if (current.equals("stopped")) {
          name = movieName
          current = "plying"
          // todo send message to blinds to notify it with the plying mode
          notifyBlindsOfMediaStationState()
          println(s"the movie $name is now plying")
        }
        if (current.equals("plying") && !name.equals(movieName))
          println(s"The movie $name is still plying, you should stop it in order to start the movie $movieName")
      case StopCurrentMovie =>
        current = "stopped"
        // todo send message to blinds to notify it with the stopped mode
        notifyBlindsOfMediaStationState()
        println(s"$name has stopped plying")
        name=""
      case GetState =>
        if(current.equals("plying"))
          println(s"the movie $name is plying")
        if(current.equals("stopped"))
          println(s"the movie $name is stopped")
    }
    def notifyBlindsOfMediaStationState(): Unit ={
      val blinds=system.actorOf(Props[Blinds])
      blinds ! GetMediaStationState(current)
    }
  }

  val system=ActorSystem("MediaStation")
  val mediaStation=system.actorOf(Props[MediaStation])
  mediaStation ! GetState
  mediaStation ! PlayMovie("on the wall")
  mediaStation ! PlayMovie("gogo")
  mediaStation ! GetState
  mediaStation ! StopCurrentMovie
//  mediaStation ! PlayMovie("AnOf")
}
