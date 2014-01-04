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
import akka.pattern.pipe
import play.api.libs.json._
import play.api.libs.json.Json._
import models.Tweet
import models.Tweet._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.util.{ Try, Success, Failure }
import scala.concurrent.Future

class StreamRecorder extends Actor {

  import StreamRecorder._
  import OpinionFinder._
  import Tweet._
  var count = 0
  def receive = {

    case StartRecording(tokens, term) =>
      val client = sender
      Profile.findOrCreate(term) onComplete {
        case Success(p) =>
          client ! RecordingStarted
          self ! Record(tokens, term, client)
        case Failure(t) => self ! RecordingFailed
      }

    case Record(tokens, term, client) => {
      //Define an Iteratee send each list of tweets to Opinion finder actor
      val sendToOpinionFinder = Iteratee.foreach[List[Tweet]](list => {
        
        OpinionFinder.ref ! Find(term, list: _*)
      })
      //Define an Enumratee that transform Byte tweets into a list of Objects 
      val arrayToTweet: Enumeratee[Array[Byte], List[Tweet]] = Enumeratee.map[Array[Byte]](arr => {
        count += 1
        val o: JsValue = Json.parse(new String(arr))
        val t: Tweet = Tweet.tweetRead.reads(o).get
        List(t)
      })
      //Iteratee to manage tweets stream
      val wsIteratee = arrayToTweet &>> sendToOpinionFinder
      //Open twitter streaming pipe
      WS.url("https://stream.twitter.com/1/statuses/filter.json").withQueryString("track" -> term)
        .withRequestTimeout(100000000)
        .sign(OAuthCalculator(Twitter.KEY, tokens))
        .get(_ => wsIteratee)
    }

  }
}
object StreamRecorder {
  trait Event
  trait Reply
  case class StartRecording(tokens: RequestToken, term: String) extends Event
  case class StopRecording(term: String) extends Event
  case class Save(tweet: Tweet*) extends Event
  case class Record(tokens: RequestToken, term: String, client: ActorRef) extends Event
  case object RecordingFailed extends Reply
  case object RecordingStarted extends Reply
  //case class Init(expression:String)
  lazy val ref = Akka.system.actorOf(Props[StreamRecorder])
}
