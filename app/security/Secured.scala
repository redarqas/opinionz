package security

import play.api.mvc.Security._
import scala.concurrent.Future
import play.Logger
import play.api._
import play.api.libs.ws._
import play.api.libs.oauth._
import play.api.mvc._
import play.api.libs._
import play.api.libs.concurrent._
import play.api.libs.iteratee._

object Secured {

  /** ======= OAuth1 ======= **/
  //Define Opinionz application Key
  val KEY = ConsumerKey("f3uBW3ySq3UtjVrnBOfA", "GoAkZKtxHx67uvejG9I2drqC8c342KZQoYn2b30g")
  //Define Twitter OAuth endpoints
  val TWITTER = OAuth(ServiceInfo(
    "https://api.twitter.com/oauth/request_token",
    "https://api.twitter.com/oauth/access_token",
    "https://api.twitter.com/oauth/authorize", KEY))

  class AuthenticatedRequest[A](val requestToken: RequestToken,
    request: Request[A]) extends WrappedRequest[A](request)

  object Authenticated extends ActionBuilder[AuthenticatedRequest] {
    def userinfo(request: RequestHeader): Option[RequestToken] = {
      for {
        token <- request.session.get("token")
        secret <- request.session.get("secret")
      } yield RequestToken(token, secret)
    }

    def onUnauthorized(request: RequestHeader): SimpleResult = Results.Redirect(controllers.routes.Twitter.authenticate())

    def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[SimpleResult]) = {
      AuthenticatedBuilder(userinfo, onUnauthorized).authenticate(request, { authRequest: Security.AuthenticatedRequest[A, RequestToken] =>
        block(new AuthenticatedRequest[A](authRequest.user, request))
      })
    }
  }

}
