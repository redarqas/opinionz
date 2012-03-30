package actors

import play.Logger

import akka.actor._
import play.libs.Akka
import services.Sentiment
import actors.ProfileWorker.NewTweets
import org.bson.types.ObjectId
import com.mongodb.casbah.commons.MongoDBObject
import play.api.libs.concurrent.Promise
import models.{Opinion, Profile, Tweet}


class OpinionFinder extends Actor {
   import OpinionFinder._


   def receive = {
      case Find(term, tweet @ _*) => {
         Logger.debug("OpinionFinder received Find(...)")
         val promises:Seq[Promise[(Tweet,Opinion)]] = tweet.map(t => Sentiment.getSentiment(t.text).map(o => (t, o)))
         Logger.debug("Fetched "+promises.size+ " opinion analysis")
         val tweetsWithOpinion = Promise.sequence(promises)
               .map(_.map(two => two._1.copy(opinion = Some(two._2))))
         
         tweetsWithOpinion.map { tws =>
            ProfileWorker.ref ! NewTweets(term, tws:_*)

            Profile.findOne(MongoDBObject("expression" -> term))  //FIX-ME what s
                  .map(_.id)
                  .map{ profileId =>
                     tws.map(t => Tweet.insert(t.copy(profileId = Some(profileId))) ) //
                  }
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
