package models

import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoConnection
import play.modules.mongodb._
import play.api.libs.json._
import play.api.Play.current
import java.util.Date

case class Opinion(text: String, mood: String, prob: Double, date: Date = new Date()) {}

//case class Tweet(message:String, source:String, hashs:List[String])

object Opinion extends SalatDAO[Opinion, ObjectId](collection = MongoPlugin.collection("opinions")) {

  implicit object OpinionFormat extends Format[Opinion] {
    def reads(json: JsValue): Opinion = Opinion(
      (json \ "text").as[String],
      (json \ "mood").as[String],
      (json \ "prob").as[Double])

    def writes(o: Opinion): JsValue = JsObject(Seq(
      "text" -> JsString(o.text),
      "mood" -> JsString(o.mood),
      "prob" -> JsNumber(o.prob),
      "date" -> JsNumber(o.date.getTime)))
  }
}

