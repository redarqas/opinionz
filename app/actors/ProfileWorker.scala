package actors

import play.api._
import play.api.libs.iteratee._
import play.Logger
import akka.actor._
import akka.actor.Actor._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current
import play.libs.Akka
import models.{ Tweet, Opinion }
import scala.concurrent.Future
import play.api.libs.json.JsValue

class ProfileWorker extends Actor {

  import ProfileWorker._
  val (out, channel) = Concurrent.broadcast[(String, Tweet)]
  def receive = {

    case Listen(term) =>
      val termaTee: Enumeratee[(String, Tweet), Tweet] = Enumeratee.collect {
        case (t, tw) if t == term => tw
      }
      sender ! TweetStream(out &> termaTee)

    case NewTweets(term, tweets @ _*) =>
      tweets.foreach(t => channel push (term, t))

  }
}
object ProfileWorker {
  trait Event
  case class Listen(term: String) extends Event
  case class NewTweets(term: String, o: Tweet*)
  case class TweetStream(out: Enumerator[Tweet])
  lazy val ref = Akka.system.actorOf(Props[ProfileWorker])
  
  def join(term: String) : Future[(Iteratee[JsValue, _], Enumerator[JsValue])] = ???
  
}
