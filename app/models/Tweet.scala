package models
import java.util.Date
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import play.modules.mongodb._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.libs.json.JsArray
import play.api.Play.current

//TODO : add properties
case class Tweet(profileId:Option[ObjectId], text: String, opinion: Option[Opinion] = None, json: Option[DBObject] = None) {}

object Tweet extends SalatDAO[Tweet, ObjectId](collection = MongoPlugin.collection("tweet")) {

  implicit object TweetFormat extends Format[Tweet] {
    def reads(json: JsValue): Tweet = Tweet(
      None,
      (json \ "text").as[String],
      None
    )
    def writes(tweet: Tweet): JsObject = JsObject(Seq(
      "profileId" -> JsString(tweet.profileId.toString),
      "text" -> JsString(tweet.text),
      "opinion" -> toJson(tweet.opinion)
    ))

  }
}