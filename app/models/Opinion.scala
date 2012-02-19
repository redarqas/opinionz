package models
import play.api.libs.json._

case class Opinion(text: String, mood: String, prob: Double) {}

object OpinionFormat extends Format[Opinion] {
  def reads(json: JsValue): Opinion = Opinion(
    (json \ "text").as[String],
    (json \ "mood").as[String],
    (json \ "prob").as[Double]
   )

  def writes(o: Opinion): JsValue = JsObject(Seq(
    "text" -> JsString(o.text),
    "mood" -> JsString(o.mood),
    "prob" -> JsNumber(o.prob))
   )
}