package actors


import play.api._
import libs.oauth.OAuthCalculator._
import libs.oauth.{OAuthCalculator, RequestToken}
import libs.ws.WS
import play.api.libs.iteratee._
import play.api.libs.iteratee.Enumerator.Pushee
import play.Logger

import akka.actor._
import akka.actor.Actor._
import play.api.libs.concurrent._
import play.api.Play.current
import play.libs.Akka
import models._
import controllers.Twitter
import akka.pattern.ask



class StreamRecorder extends Actor {
   import StreamRecorder._
   import OpinionFinder._

   def receive = {
      case StartRecording(token, term) => {

         WS.url("https://stream.twitter.com/1/statuses/filter.json")
               .withQueryString("track" -> term)
               .sign(OAuthCalculator(Twitter.KEY, token))
            .get()
               .map(r => {
                  Logger.debug(r.body)
                  val json = r.json

               })

              //OpinionFinder.ref ? Find(tweet)
              // ajouter l'opinion dans le tweet du profil qu'on était en train d'enregister.
      }
         
         

      case StopRecording(term) => {
         Logger.info("Stop recording "+term)
      }
      case _ => {
         Logger.info("error matching actor message")
      }
   }
}
object StreamRecorder {
   trait Event
   case class StartRecording(tokens:RequestToken, term:String) extends Event
   case class StopRecording(term:String) extends Event
   //case class Init(expression:String)
   lazy val ref = Akka.system.actorOf(Props[StreamRecorder])
}
