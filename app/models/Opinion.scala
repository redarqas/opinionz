package models

import play.api.libs.json._
import play.api.Play.current

case class Opinion(mood: String, prob: Double) {}

object Opinion {
  implicit val opinionRead = Json.reads[Opinion]
  implicit val opinionWrite = Json.writes[Opinion]
}

