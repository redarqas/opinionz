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
import models.{Tweet, Opinion}


class ProfileWorker extends Actor {
   import ProfileWorker._
   var listeners : List[Tuple2[String,Pushee[Tweet]]] = Nil

   def receive = {
      case Listen(term) => {
         lazy val channel: Enumerator[Tweet] = Enumerator.pushee(
            pushee => self ! Init(pushee, term),
            onComplete = self ! Quit()
         )
         Logger.info("New opinion stream on")
         sender ! channel
      }
      case Init(pushee,term) => {
         listeners = listeners :+ (term -> pushee)
      }

      case Quit() => {
         Logger.info("Opinion stream stopped ...")
      }

      case NewTweets(term, tweets @ _*) => {
         Logger.info("New opinion : " + tweets.toString)
         listeners.filter(_._1 == term).foreach(p => tweets.foreach( t => p._2.push(t)))
      }
      case _ => {
         Logger.info("error matching actor message")
      }
   }
}
object ProfileWorker {
   trait Event
   case class Listen(term:String) extends Event
   case class Quit() extends Event
   case class NewTweets(term:String ,o:Tweet*)
   case class Init(p:Pushee[Tweet], term:String)
   lazy val ref = Akka.system.actorOf(Props[ProfileWorker])
}
