package models

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

/**
 * Case class for Profile document
 */
case class Profile(@Key("_id") id: ObjectId = new ObjectId, expression: String) {

   def tweets = Profile.tweets.findByParentId(this.id).toList

}
/**
 * JSON formatter
 */

object Profile extends SalatDAO[Profile, ObjectId](collection = MongoPlugin.collection("profile")) {

  val tweets = new ChildCollection[Tweet, ObjectId](collection = MongoPlugin.collection("tweet"), parentIdField = "profileId") {}

  implicit object ProfileFormat extends Writes[Profile] {
    def writes(profile: Profile) = JsObject(Seq(
      "id" -> JsString(profile.id.toString),
      "expression" -> JsString(profile.expression),
      "tweets" -> JsArray(profile.tweets.map(toJson(_)))))
  }

  def byTerm(expression:String) = findOne(MongoDBObject("expression" -> expression))
}
case class Statistics()
