package part2actors

import akka.actor.AbstractActor.Receive
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
//import part2actors.ChildActor1.Parent.tellChild

object ChildActor1 extends App {
//
//  val system=ActorSystem("ChildActor1")
//
//  object Parent{
//    case class createChild(name:String)
//    case class tellChild(message:String)
//  }
//  class Parent extends Actor{
//    import Parent._
//
//    override def receive: Receive = {
//      case createChild(name) =>
//        val childRef=system.actorOf(Props[Child],name )
////        context become talkToChild(childRef)
//    }
//  }
//  def talkToChild(ref: ActorRef): Receive={
//    case tellChild(msg)=> ref forward msg
//  }
//
//  class Child extends Actor{
//    override def receive: Receive ={
//      case message => println(s"${self.path} message received $message")
//    }
//  }
}
