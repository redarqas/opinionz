package models
import java.util.Date
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.Play.current
import play.Logger
import play.api.libs.functional.syntax._
import play.api.libs.functional.syntax._
import reactivemongo.api._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONDocument
import play.modules.reactivemongo.ReactiveMongoPlugin._
import play.modules.reactivemongo.json.collection.JSONCollection

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

case class HashTag(text: Option[String])

object HashTag {
  implicit val hashTagWrite = new Writes[HashTag] {
    def writes(tag: HashTag): JsValue = JsObject(Seq(
      "text" -> JsString(tag.text.get)))
  }
  implicit val hashTagRead = Json.reads[HashTag]
}

case class Entity(hashtags: List[HashTag]) {}
object Entity {
  implicit val entityWrite = Json.writes[Entity]
  implicit val entityRead = Json.reads[Entity]
}

case class User(id: Long,
  screen_name: Option[String] = None,
  profile_image_url: Option[String] = None,
  followers_count: Option[Long] = None,
  friends_count: Option[Long] = None,
  statuses_count: Option[Long] = None) {}

object User {
  implicit val userRead = Json.reads[User]
  implicit val userWrite = Json.writes[User]
}

//TODO : add properties
case class Tweet(profileId: Option[BSONObjectID],
  text: String,
  opinion: Option[Opinion] = None,
  json: Option[BSONDocument] = None,
  retweet_count: Option[Long] = None,
  created_at: Option[Date] = None,
  id: Option[Long] = None,
  user: Option[User] = None,
  entities: Option[Entity] = None) {}

object Tweet { 
  val dateFormat: java.text.SimpleDateFormat = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", java.util.Locale.ENGLISH)

  implicit val tweetRead = (
    (__ \ "text").read[String] and
    (__ \ "retweet_count").readNullable[Long] and
    (__ \ "created_at").readNullable[String].map(d => d.map(t => dateFormat.parse(t))) and
    (__ \ "id").readNullable[Long] and
    (__ \ "user").readNullable[User] and
    (__ \ "entities").readNullable[Entity])((text, retweet_count, created_at, id, user, entities) =>
      Tweet(None, text, None, None, retweet_count, created_at, id, user, entities))
  implicit val tweetWrite = new Writes[Tweet] {
    def writes(tweet: Tweet): JsValue = JsObject(Seq(
      "profileId" -> JsString(tweet.profileId.toString),
      "text" -> JsString(tweet.text),
      "opinion" -> toJson(tweet.opinion),
      "retweet_count" -> JsNumber(tweet.retweet_count.get),
      "created_at" -> JsString(dateFormat.format(tweet.created_at.get)),
      "id" -> JsNumber(tweet.id.get),
      "user" -> toJson(tweet.user),
      "entities" -> toJson(tweet.entities)))
  }

  /*def incMapReduce(profile: Profile) = {
    Logger.debug("New MapReduce Operation")
    val MERGE_COLLECTION: String = "globalStatistics"

    val maybeLastIncrement: Future[Option[Tweet]] = db.collection[JSONCollection](MERGE_COLLECTION)
    .find(Json.parse("{}")).sort(Json.obj("date" -> -1)).one
    
    val maybeLastIncrementDate = maybeLastIncrement.flatMap(o => )

    val query = maybeLastIncrementDate.map(d => ("date" $gt d) ++ Profile.tweets.parentIdQuery(profile.id)).getOrElse(Profile.tweets.parentIdQuery(profile.id))

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

    MongoPlugin.collection("tweet").mapReduce(mapF, reduceF, MapReduceMergeOutput(MERGE_COLLECTION), finalizeFunction = Some(finalizeF), query = Some(query), verbose = true)

  }*/
}
