package services

import models._
import play.api.libs.ws.WS
import play.api.libs.json.Json._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Sentiment {
  /**
   * Get opinion
   */
  def getSentiment(text: String): Future[Opinion] = {
    WS.url(sentimentUrl).withQueryString(("api_key", key), ("text", text))
      .get()
      .map(_.json.as[Opinion])
  }
  /**
   * Add new sentence to repo
   */
  def train(text: String, mood: String): Future[Boolean] = {
    WS.url(trainUrl).withQueryString(("api_key", key), ("text", text), ("mood", mood))
      .get()
      .map { response => (response.json \ "status").as[String] == "ok" }
  }
  /**
   * Check quota
   */
  def checkQuota(): Future[Long] = {
    WS.url(quotaUrl).withQueryString(("api_key", key))
      .get()
      .map { response => (response.json \ "quota_remaining").as[Long] }
  }
}