package actors


import play.api._
import play.api.libs.iteratee._
import play.api.libs.iteratee.Enumerator.Pushee
import play.Logger

import akka.actor._
import akka.actor.Actor._
import play.api.libs.concurrent._
import play.api.Play.current
import play.libs.Akka
import models.Opinion


class ProfileWorker extends Actor {
   import ProfileWorker._
   var listeners : List[Pushee[Opinion]] = Nil

   def receive = {
      case Listen() => {
         lazy val channel: Enumerator[Opinion] = Enumerator.pushee(
            pushee => self ! Init(pushee),
            onComplete = self ! Quit()
         )
         Logger.info("New opinion stream on")
         sender ! channel
      }
      case Init(pushee) => {
         listeners = listeners :+ pushee
      }

      case Quit() => {
         Logger.info("Opinion stream stopped ...")
      }

      case NewOpinion(opinion) => {
         Logger.info("New opinion : " + opinion.toString)
         listeners.foreach(_.push(opinion))
      }
      case _ => {
         Logger.info("error matching actor message")
      }
   }
}
object ProfileWorker {
   trait Event
   case class Listen() extends Event
   case class Quit() extends Event
   case class NewOpinion(o:Opinion)
   case class Init(p:Pushee[Opinion])
   lazy val ref = Akka.system.actorOf(Props[ProfileWorker])

}
