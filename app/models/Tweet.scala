package models
import play.api.libs.json._
import play.api.libs.json.Json._


//TODO : add properties
case class Tweet(text: String, opinion: Opinion) {}

object Tweet {
  implicit object TweetFormat extends Format[Tweet] {
    def reads(json: JsValue): Tweet = Tweet(
      (json \ "text").as[String],
      (json \ "opinion").as[Opinion]
    )

    def writes(tweet: Tweet): JsObject = JsObject(Seq(
      "text" -> JsString(tweet.text),
      "opinion" -> toJson(tweet.opinion)
    ))

  }
}