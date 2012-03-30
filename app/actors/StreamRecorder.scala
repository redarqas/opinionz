package actors

import play.api._
import libs.oauth.OAuthCalculator._
import libs.oauth.{ OAuthCalculator, RequestToken }
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
import play.api.libs.json._
import play.api.libs.json.Json._
import models.Tweet
import models.Tweet._
import play.modules.mongodb.MongoJson
import com.mongodb.DBObject

class StreamRecorder extends Actor {
  import StreamRecorder._
  import OpinionFinder._

  def receive = {
    case StartRecording(tokens, term) => {
       Logger.debug("StreamRecorder received StartRecording("+tokens.toString()+","+term+")")
      //Define an Iteratee send each list of tweets to Opinion finder actor
      val sendToOpinionFinder = Iteratee.foreach[List[Tweet]](list => {
         OpinionFinder.ref ! Find(term, list: _*)
      })
      //Define an Enumratee that transform Byte tweets into a list of Objects 
      val arrayToTweet: Enumeratee[Array[Byte], List[Tweet]] = Enumeratee.map[Array[Byte]](arr => {
        val res = new String(arr)
        Logger.debug(" --> New chunk")
        val o: JsValue = Json.parse(res)
        val t: Tweet = fromJson(o)
        Logger.debug("Deserialized tweet:"+t)
        List(t)
         /*try {
            val json: DBObject = MongoJson.fromJson(JsObject(Seq("test"-> JsString("bordel"))))
            Logger.debug(" Mongoplugin json:"+json.toString)
            List(t.copy(json = Some(json)))
         } catch {
            case e => {
               Logger.debug(e.getMessage)
               throw new RuntimeException("Error occurred",e)
            }
         }*/

      })
      //Iteratee to manage tweets stream
      val wsIteratee = arrayToTweet.transform(sendToOpinionFinder)
      //Open twitter streaming pipe
      WS.url("https://stream.twitter.com/1/statuses/filter.json?track=" + term)
        .sign(OAuthCalculator(Twitter.KEY, tokens))
        .get(_ => wsIteratee)
    }

    case StopRecording(term) => {
      Logger.info("Stop recording " + term)
    }
    case _ => {
      Logger.info("error matching actor message")
    }
  }
}
object StreamRecorder {
  trait Event
  case class StartRecording(tokens: RequestToken, term: String) extends Event
  case class StopRecording(term: String) extends Event
  case class Save(tweet: Tweet*) extends Event
  //case class Init(expression:String)
  lazy val ref = Akka.system.actorOf(Props[StreamRecorder])
}
