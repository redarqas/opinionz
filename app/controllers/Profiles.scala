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
import actors.{ProfileWorker, StreamRecorder}
import akka.util.duration._
import akka.util.Timeout
import play.api.libs.json._
import play.api.libs.json.Json._
import akka.pattern.ask
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import play.api.libs.Comet


object Profiles extends Controller {
   /** ============================ **/
   /** Form definition              **/
   /** ============================ **/
   val profileFrom: Form[Profile] =  Form(
       mapping(
         "text" -> text
       ) {
         expression => Profile(expression, Nil)
       } {
         profile => Some(profile.expression)
       }
   )
   
   /** ============================ **/
   /** Actions defintion            **/
   /** ============================ **/

   //Create a profile and start streaming
   def create = Action { implicit request => 
     profileFrom.bindFromRequest.fold(
         errors => {
            Ok(views.html.profiles.form("Ask me ! ", errors))
         },
         profile =>  {

           Profile.insert(profile)
           val tokens = Twitter.sessionTokenPair(request).get

           StreamRecorder.ref ! StartRecording(tokens, profile.expression)

           Ok("Now recording" + profile.expression)

         }
     )
   }
   
   //Display form to create profile
   def index = Action { implicit request =>
     Ok(views.html.profiles.form("Ask me ! ", profileFrom))
   }


   val cometEnumeratee = Comet(callback = "window.parent.signIt")(Comet.CometMessage[Opinion](signer => {
      Logger.debug("converting to json")
      toJson(signer).toString
   }))

   def stream = Action {
      import ProfileWorker._
      AsyncResult {
		implicit val timeout = Timeout(1 second)
         (ProfileWorker.ref ? Listen()).mapTo[Enumerator[Opinion]].asPromise.map {
			chunks => {
               Logger.debug("un chunk")
               Ok.stream(chunks &> cometEnumeratee)
            }
         }
      }
   }
}