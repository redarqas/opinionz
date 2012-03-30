package models
import java.util.Date
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import play.modules.mongodb._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import play.modules.mongodb._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.Play.current
import play.Logger

case class HashTag(text: Option[String])

object HashTag {
  implicit object HashTagFormat extends Format[HashTag] {
    def reads(json: JsValue) = {
      HashTag((json \ "text").asOpt[String])
    }
    def writes(tag: HashTag): JsObject = JsObject(Seq(
      "text" -> JsString(tag.text.get)))
  }
}

case class Entity(hashtags: List[HashTag]) {}
object Entity {
  implicit object EntityFormat extends Format[Entity] {
    def reads(json: JsValue) = {
      Entity((json \ "hashtags").as[List[HashTag]])
    }
    def writes(entity: Entity): JsObject = JsObject(Seq(
      "text" -> JsArray(entity.hashtags.map(toJson(_)))))
  }

}
case class User(id: Long,
  screen_name: Option[String] = None,
  profile_image_url: Option[String] = None,
  followers_count: Option[Long] = None,
  friends_count: Option[Long] = None,
  statuses_count: Option[Long] = None) {}

object User {
  implicit object UserFormat extends Format[User] {
    def reads(json: JsValue) = {
      User((json \ "id").as[Long],
        (json \ "screen_name").asOpt[String],
        (json \ "profile_image_url").asOpt[String],
        (json \ "followers_count").asOpt[Long],
        (json \ "friends_count").asOpt[Long],
        (json \ "statuses_count").asOpt[Long])
    }
    def writes(user: User): JsObject = JsObject(Seq(
      "id" -> JsNumber(user.id),
      "screen_name" -> JsString(user.screen_name.get),
      "profile_image_url" -> JsString(user.profile_image_url.get),
      "followers_count" -> JsNumber(user.followers_count.get),
      "friends_count" -> JsNumber(user.friends_count.get),
      "statuses_count" -> JsNumber(user.statuses_count.get)))
  }
}

//TODO : add properties
case class Tweet(profileId: Option[ObjectId],
  text: String,
  opinion: Option[Opinion] = None,
  json: Option[DBObject] = None,
  retweet_count: Option[Long] = None,
  created_at: Option[Date] = None,
  id: Option[Long] = None,
  user: Option[User] = None,
  entities: Option[Entity] = None) {}

object Tweet extends SalatDAO[Tweet, ObjectId](collection = MongoPlugin.collection("tweet")) {
  val dateFormat: java.text.SimpleDateFormat  = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", java.util.Locale.ENGLISH)

  implicit object TweetFormat extends Format[Tweet] {
    def reads(json: JsValue): Tweet = Tweet(
      None,
      (json \ "text").as[String],
      None,
      None,
      (json \ "retweet_count").asOpt[Long],
      (json \ "created_at").asOpt[String].map(t => dateFormat.parse(t)),
      (json \ "id").asOpt[Long],
      (json \ "user").asOpt[User],
      (json \ "entities").asOpt[Entity])
    def writes(tweet: Tweet): JsObject = JsObject(Seq(
      "profileId" -> JsString(tweet.profileId.toString),
      "text" -> JsString(tweet.text),
      "opinion" -> toJson(tweet.opinion),
      "retweet_count" -> JsNumber(tweet.retweet_count.get),
      "created_at" -> JsString(dateFormat.format(tweet.created_at.get)),
      "id" -> JsNumber(tweet.id.get),
      "user" -> toJson(tweet.user),
      "entities" -> toJson(tweet.entities)))

  }

  def incMapReduce(profile:Profile) = {
     Logger.debug("New MapReduce Operation")
     val MERGE_COLLECTION: String = "globalStatistics"

     val maybeLastIncrement = MongoPlugin.collection(MERGE_COLLECTION).find(MongoDBObject()).sort(MongoDBObject("date" -> -1)).limit(1).toList.headOption
        val maybeLastIncrementDate = maybeLastIncrement.flatMap(_.getAs[Date]("lastIncrement"))

        val query = maybeLastIncrementDate.map(d => ("date" $gt d) ++ Profile.tweets.parentIdQuery(profile.id) ).getOrElse(Profile.tweets.parentIdQuery(profile.id))

        val mapF = """function() {
            var toMap = {
               positive : (this.opinion.mood == "positive" && this.opinion.prob >= 0.7)  ? 1 : 0  ,
               negative : (this.opinion.mood == "negative" && this.opinion.prob >= 0.7)  ? 1 : 0  ,
               neutral  : (this.opinion.prob < 0.7) ? 1 : 0  ,
               count : 1  ,
               profileId: this.profileId  ,
               last : this.date.getTime()
            }
            emit(this.profileId, toMap)
            }"""
        val reduceF = """function(key, values) {

         var result = { positive : 0 , negative : 0 , neutral : 0, last : 0 , count : 0, profileId: key};
         values.forEach(function(p) {
            result.last = Math.max(p.last,result.last);
            result.count += p.count;
            result.positive += p.positive;
            result.negative += p.negative;
            result.neutral += p.neutral;
         });

         return result;
      }"""
        val finalizeF = """function(key, value) {
         // this ugly shit is here in order to have consistant types returned (all float)
         value.neutral += 0.0;
         value.positive += 0.0;
         value.negative += 0.0;
         value.count += 0.0;

         if (value.count > 0 ) {
            var last = new Date();
            last.setTime(value.last);
            value.last = last;
         }
         return value;
      }
      """

        MongoPlugin.collection("tweet").mapReduce(mapF,reduceF,MapReduceMergeOutput(MERGE_COLLECTION),finalizeFunction = Some(finalizeF), query = Some(query), verbose = true)

     }
}
