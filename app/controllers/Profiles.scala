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
import scala.concurrent.Future
import security.Secured._
import play.api.Logger

object Profiles extends Controller {

  val profileFrom: Form[Profile] = Form(
    mapping("text" -> text) { term => Profile(expression = term) } { profile => Some(profile.expression) })

  //Display form to create profile
  def index = Authenticated { implicit request =>
    Ok(views.html.profiles.form("Ask me ! ", profileFrom))
  }

  def search = Authenticated.async { implicit request =>
    import StreamRecorder._
    profileFrom.bindFromRequest.fold(
      //Form with validation errors case
      errors => Future.successful(Ok(views.html.profiles.form("There is some errors ", errors))),
      profile => {
        val tokens = request.requestToken
        implicit val timeout = Timeout(5 seconds)
        (StreamRecorder.ref ? StartRecording(tokens, profile.expression)) map {
          case RecordingStarted => Redirect(routes.Profiles.find(profile.expression))
          case RecordingFailed => BadRequest("Unable to start recording")
        }
      })
  }

  def find(term: String) = Authenticated.async { implicit request =>
    Profile.findOne(term).map { profile =>
      Ok(views.html.profiles.stream("Now recording : ", profile))
    } recover {
      case e => BadRequest("Unable to retrieve stream")
    }

  }

  val cometEnumeratee = Comet(callback = "window.parent.streamit")(Comet.CometMessage[Tweet](signer => {
    toJson(signer).toString
  }))

  def stream(term: String) = Authenticated.async {
    import ProfileWorker._
    implicit val timeout = Timeout(5 seconds)
    (ProfileWorker.ref ? Listen(term)) map {
      case TweetStream(chunks) => Ok.chunked(chunks &> cometEnumeratee)
    }
  }
}