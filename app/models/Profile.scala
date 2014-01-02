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
import java.util.Date

/**
 * Case class for Profile document
 */
case class Profile(@Key("_id") id: ObjectId = new ObjectId, expression: String) {

  def tweets = Profile.tweets.findByParentId(this.id).toList
  def tweetsAfter(d: Date) = Profile.tweets.findByParentId(this.id, ("date" $gt d))

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

  def all = find(MongoDBObject()).toList
  def byTerm(expression: String) = findOne(MongoDBObject("expression" -> expression))

}

/*

sealed trait Statistics

case class Daily(from:Date, value:MoodTotal) extends Statistics
case class Weekly(from:Date, value:MoodTotal) extends Statistics
case class Monthly(from:Date, value:MoodTotal) extends Statistics
case class Overall(value:MoodTotal, lastIncrement:Date) extends Statistics

case class MoodTotal(positive:Int,neutral:Int,negative:Int) {
   def +(that:MoodTotal) = MoodTotal(
         this.positive + that.positive,
         this.neutral + that.neutral,
         this.negative + that.negative
   )
}
object MoodTotal {

}
*/

