package controllers

import views._
import models._
import services._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.oauth._
import play.api.libs.concurrent._
import play.api.libs.iteratee._
import play.api.libs._
import play.api.libs.ws._
import play.api.libs.oauth.OAuthCalculator
import play.Logger
import actors.StreamRecorder._
import akka.util.Timeout
import actors.{ ProfileWorker, StreamRecorder }
import akka.util.duration._
import akka.util.Timeout
import play.api.libs.json._
import play.api.libs.json.Json._
import akka.pattern.ask
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.libs.Comet

object Profiles extends Controller {
  /** ====== Form definition ====== **/
  val profileFrom: Form[Profile] = Form(
    mapping(
      "text" -> text) {
        expression => Profile(expression, Nil)
      } {
        profile => Some(profile.expression)
      })

  /** ====== Actions defintion ====== **/
  //Display form to create profile
  def index = Action { implicit request =>
    Ok(views.html.profiles.form("Ask me ! ", profileFrom))
  }

  //Create a profile and start streaming
  def create = Action { implicit request =>
    profileFrom.bindFromRequest.fold(
      //Form with validation errors case
      errors => {
        Ok(views.html.profiles.form("There is some errors ", errors))
      },
      profile => {
        //Create a profile on mongodb
        Profile.insert(profile)
        //Retrieve Twitter Oauth tokens 
        val tokens = Twitter.sessionTokenPair(request).get
        //Launch Tweets recorder
        StreamRecorder.ref ! StartRecording(tokens, profile.expression)
        Ok(views.html.profiles.stream("Now recording : " , profile))
        //Redirect(Profiles.stream(profile.expression)).withSession("token" -> t.token, "secret" -> t.secret)
      })
      
  }

  /** ====== Define stream results  ====== **/
  val cometEnumeratee = Comet(callback = "window.parent.streamit")(Comet.CometMessage[Tweet](signer => {
    toJson(signer).toString
  }))

  def stream(term: String) = Action {
    import ProfileWorker._
    AsyncResult {
      implicit val timeout = Timeout(1 second)
      (ProfileWorker.ref ? Listen(term)).mapTo[Enumerator[Tweet]].asPromise.map {
        chunks =>
          {
            Ok.stream(chunks &> cometEnumeratee)
          }
      }
    }
  }
}