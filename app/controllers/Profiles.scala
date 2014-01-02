package controllers

import play.Logger
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs._
import play.api.libs.ws._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.oauth._
import play.api.libs.oauth.OAuthCalculator
import play.api.libs.Comet

import scala.concurrent.duration._
import scala.language.postfixOps

import akka.util.Timeout
import akka.pattern.ask

import actors.{ ProfileWorker, StreamRecorder }
import actors.StreamRecorder._

import models._
import services._
import views._

object Profiles extends Controller {

  /** ====== Form definition ====== **/
  val profileFrom: Form[Profile] = Form(
    mapping("text" -> text) { term => Profile(expression = term) } { profile => Some(profile.expression) })

  /** ====== Actions defintion ====== **/
  //Display form to create profile
  def index = Action { implicit request =>
    Ok(views.html.profiles.form("Ask me ! ", profileFrom))
  }

  //Create a profile and start streaming
  def search = Action { implicit request =>
    profileFrom.bindFromRequest.fold(
      //Form with validation errors case
      errors => {
        Ok(views.html.profiles.form("There is some errors ", errors))
      },
      profile => {
        Logger.debug("Find or create :" + profile.expression)
        Profile //FIX-ME this should move inside StreamRecorder
          .byTerm(profile.expression) // retrieve existing profile if any
          .orElse { Profile.insert(profile).map(i => profile.copy(id = i)) } //or create a new one in mongo
          .toRight(InternalServerError) // if still no profile, fail
          .right.map { p =>
            //Retrieve Twitter Oauth tokens
            val tokens = Twitter.sessionTokenPair(request).get
            //Launch Tweets recorder
            Logger.debug("")
            StreamRecorder.ref ! StartRecording(tokens, p.expression)
            p
          }.fold(identity, p => Redirect(routes.Profiles.find(p.expression)))
      })
  }
  def find(term: String) = Action { implicit request =>
    Profile.byTerm(term)
      .toRight(NotFound)
      .fold(identity, p => Ok(views.html.profiles.stream("Now recording : ", p)))
  }

  /** ====== Define stream results  ====== **/
  val cometEnumeratee = Comet(callback = "window.parent.streamit")(Comet.CometMessage[Tweet](signer => {
    toJson(signer).toString
  }))

  def stream(term: String) = Action.async {
    import ProfileWorker._
    implicit val timeout = Timeout(5 seconds)
    (ProfileWorker.ref ? Listen(term)) map {
      case TweetStream(chunks) => Ok.chunked(chunks &> cometEnumeratee)
    }
  }
}