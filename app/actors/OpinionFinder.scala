package actors

import play.Logger
import akka.actor._
import play.libs.Akka
import services.Sentiment
import actors.ProfileWorker.NewTweets
import models.{ Opinion, Profile, Tweet }
import scala.concurrent.Promise
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class OpinionFinder extends Actor {
  import OpinionFinder._

  def receive = {
    case Find(term, tweet @ _*) => {

      val updatedTweets: Seq[Future[Tweet]] = tweet.map { t =>
        Sentiment.getSentiment(t.text).map(result => t.copy(opinion = Some(result)))
      }
      val tweetsWithOpinion: Future[Seq[Tweet]] = Future.sequence(updatedTweets)

      tweetsWithOpinion.map { tws =>
        ProfileWorker.ref ! NewTweets(term, tws: _*)
        //TODO : Insert tweet for this profile
        //        Profile.findOne(MongoDBObject("expression" -> term)) //FIX-ME what s
        //          .map(_.id)
        //          .map { profileId =>
        //            tws.map(t => Tweet.insert(t.copy(profileId = Some(profileId)))) //
        //          }
      }
    }
    case _ => {
      Logger.info("error matching actor message")
    }
  }
}

object OpinionFinder {
  trait Event
  case class Find(term: String, tweet: Tweet*) extends Event
  lazy val ref = Akka.system.actorOf(Props[OpinionFinder])
}
