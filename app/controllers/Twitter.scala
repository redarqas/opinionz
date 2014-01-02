package controllers

import play.api._
import play.api.mvc._
import models._

/**
 * User: alabbe
 * Date: 21/02/12
 * Time: 21:32
 */

import views._

import play.Logger
import play.api._
import play.api.libs.ws._
import play.api.libs.oauth._
import play.api.mvc._
import play.api.libs._
import play.api.libs.concurrent._
import play.api.libs.iteratee._

object Twitter extends Controller {

  /** ======= OAuth1 ======= **/
  //Define Opinionz application Key
  val KEY = ConsumerKey("f3uBW3ySq3UtjVrnBOfA", "GoAkZKtxHx67uvejG9I2drqC8c342KZQoYn2b30g")
  //Define Twitter OAuth endpoints
  val TWITTER = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authorize", KEY))

  //Call Twitter OAuth service
  def authenticate = Action { request =>
    request.queryString.get("oauth_verifier").flatMap(_.headOption).map { verifier =>
      val tokenPair = sessionTokenPair(request).get
      // We got the verifier; now get the access token, store it and back to index
      TWITTER.retrieveAccessToken(tokenPair, verifier) match {
        case Right(t) => {
          // We received the unauthorized tokens in the OAuth object - store it before we proceed
          Redirect(routes.Application.index()).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      }
    }.getOrElse(
      TWITTER.retrieveRequestToken("http://localhost:9000/auth") match {
        case Right(t) => {
          // We received the unauthorized tokens in the OAuth object - store it before we proceed
          Redirect(TWITTER.redirectUrl(t.token)).withSession("token" -> t.token, "secret" -> t.secret)
        }
        case Left(e) => throw e
      })
  }

  //Utility method : retrieve (token, secret) from request header
  def sessionTokenPair(implicit request: RequestHeader): Option[RequestToken] = {
    for {
      token <- request.session.get("token")
      secret <- request.session.get("secret")
    } yield {
      RequestToken(token, secret)
    }
  }
}