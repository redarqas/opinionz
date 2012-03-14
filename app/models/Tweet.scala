package models
import play.api.libs.json._
import play.api.libs.json.Json._
import java.util.Date


//TODO : add properties
case class Tweet(text: String, opinion: Opinion = null) {}

object Tweet {

  implicit object TweetFormat extends Format[Tweet] {
    def reads(json: JsValue): Tweet = Tweet(
      (json \ "text").as[String],
      (json \ "opinion").asOpt[Opinion].getOrElse(Opinion("", "", 0, new Date()))
    )

    def writes(tweet: Tweet): JsObject = JsObject(Seq(
      "text" -> JsString(tweet.text),
      "opinion" -> toJson(tweet.opinion)
    ))

  }
}