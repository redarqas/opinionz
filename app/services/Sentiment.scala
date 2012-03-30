package services

import models._
import play.api.libs.ws.WS
import play.api.libs.json.Json._
import play.api.libs.concurrent.Promise

object Sentiment {
  /**
   * Get opinion
   */
  def getSentiment(text: String):Promise[Opinion] = {
    WS.url(sentimentUrl).withQueryString(("api_key", key), ("text", text))
      .get()
      .map(_.json.as[Opinion])
  }
  /**
   * Add new sentence to repo
   */
  def train(text: String, mood: String): Boolean = {
    WS.url(trainUrl).withQueryString(("api_key", key), ("text", text), ("mood", mood))
      .get()
      .map { response => (response.json \ "status").as[String] }
      .value.get == "ok"
  }
  /**
   * Check quota
   */
  def checkQuota(): Long = {
    WS.url(quotaUrl).withQueryString(("api_key", key))
      .get()
      .map { response => (response.json \ "quota_remaining").as[Long] }
      .value.get;
  }
}