package services
import play.api.libs.ws.WS
import models.Opinion

object TestSentiment extends App {
  println(Sentiment.getSentiment("Jamal is bad"))
  println(Sentiment.checkQuota())
}