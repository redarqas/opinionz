package actors

import play.Logger

import akka.actor._
import play.libs.Akka
import models.Tweet
import services.Sentiment


class OpinionFinder extends Actor {
   import OpinionFinder._

   def receive = {
      case Find(tweet) => {

         val o = Sentiment.getSentiment(tweet.text)
         ProfileWorker.ref ! o


      }
      case _ => {
         Logger.info("error matching actor message")
      }
   }
}

object OpinionFinder {
   trait Event
   case class Find(tweet:Tweet) extends Event
   lazy val ref = Akka.system.actorOf(Props[OpinionFinder])
}
