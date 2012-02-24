package actors

import play.Logger

import akka.actor._
import play.libs.Akka
import services.Sentiment
import actors.ProfileWorker.NewTweets
import org.bson.types.ObjectId
import models.{Profile, Tweet}
import com.mongodb.casbah.commons.MongoDBObject


class OpinionFinder extends Actor {
   import OpinionFinder._


   def receive = {
      case Find(term, tweet @ _*) => {
         val tweets = tweet.foldLeft(List[Tweet]())( (r,t) => {
            val o = Sentiment.getSentiment(t.text)
            val betterTweet = t.copy(opinion = o)
            r :+ t
         })
         ProfileWorker.ref ! NewTweets(term, tweets:_*)
         Profile.findOne(MongoDBObject("expression" -> term)).map{ p =>
            Profile.save(p.copy(tweets = p.tweets ++ tweets))
         }
      }
      case _ => {
         Logger.info("error matching actor message")
      }
   }
}

object OpinionFinder {
   trait Event
   case class Find(term:String, tweet:Tweet*) extends Event
   lazy val ref = Akka.system.actorOf(Props[OpinionFinder])
}
