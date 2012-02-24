package models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoConnection
import play.modules.mongodb._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.libs.json.JsArray
import play.api.Play.current

/**
 * Case class for Profile document
 */
case class Profile(expression: String, tweets: List[Tweet]) {}
/**
 * JSON formatter
 */

object Profile extends SalatDAO[Profile, ObjectId](collection = MongoPlugin.collection("profiles")) {

  implicit object ProfileFormat extends Format[Profile] {
    // Marshaling as JSON
    def reads(json: JsValue): Profile = Profile(
      (json \ "expression").as[String],
      (json \ "tweets").asOpt[List[Tweet]].getOrElse(List()))
    //Unmarshaling as JSON Object
    def writes(profile: Profile) = JsObject(Seq(
      "expression" -> JsString(profile.expression),
      "tweets" -> JsArray(profile.tweets.map(toJson(_)))))
  }

}