package models

import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.libs.json.JsArray
import play.api.Play.current
import java.util.Date
import play.api.libs.functional.syntax._
import reactivemongo.api._
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONDocument
import play.modules.reactivemongo.ReactiveMongoPlugin._
import play.modules.reactivemongo.json.collection.JSONCollection

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import Tweet._
import play.api.Logger

/**
 * Case class for Profile document
 */
case class Profile(_id: BSONObjectID = BSONObjectID.generate, expression: String) {
  def tweets: Future[List[Tweet]] = Profile.tweetCollection.find(Json.parse("{}")).cursor[Tweet].collect[List]()
  def tweetsAfter(d: Date): Future[List[Tweet]] = Profile.tweetCollection.find(Json.obj("date" -> Json.obj("$gt" -> d))).cursor[Tweet].collect[List]()
  def insertTweet(tweet: Tweet) =
    Profile.tweetCollection.insert(tweet.copy(profileId = Some(_id)))

}
/**
 * JSON formatter
 */

object Profile {
  implicit val profileWrite: Writes[Profile] = new Writes[Profile] {
    def writes(profile: Profile) = JsObject(Seq(
      "_id" -> JsString(profile._id.stringify),
      "expression" -> JsString(profile.expression)))
  }
  implicit val profileRead: Reads[Profile] = (
    (__ \ "_id").read[String].map(i => BSONObjectID(i)) and
    (__ \ "expression").read[String])(Profile.apply _)

  val profileCollection: JSONCollection = db.collection[JSONCollection]("profile")
  val tweetCollection: JSONCollection = db.collection[JSONCollection]("tweet")

  def all: Future[List[Profile]] = profileCollection.find(Json.parse("{}")).cursor[Profile].collect[List]()
  def findOne(expression: String): Future[Profile] = {
    profileCollection.
      find(Json.obj("expression" -> expression)).
      cursor[Profile].
      headOption map {
        case Some(e) => e
        case None => throw new Exception("Not found")
      }
  }
  def insert(term: String): Future[Profile] = {
    profileCollection.insert(Profile(expression = term)) flatMap { lastError =>
      Logger.debug("lastError " + lastError)
      findOne(term)
    } recover {
      case e => throw new Exception(e)
    }
  }

  def findOrCreate(term: String): Future[Profile] = {
    Logger.debug("findOrCreate(term: String)" + term)
    Profile.findOne(term).fallbackTo(Profile.insert(term))
  }

}