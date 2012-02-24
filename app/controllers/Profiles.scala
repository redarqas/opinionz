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


object Profiles extends Controller {
   /** ============================ **/
   /** Form definition              **/
   /** ============================ **/
   val profileFrom: Form[Profile] =  Form(
       mapping(
         "expression" -> text 
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
         errors => Ok(""),
         profile => AsyncResult {
           Profile.insert(profile)
           //TODO : redirect to streming page
           val tokens = Twitter.sessionTokenPair(request).get
           WS.url("https://stream.twitter.com/1/statuses/filter.json?track=" + profile.expression)
           .sign(OAuthCalculator(Twitter.KEY, tokens))
           .get.map(r => {
              Logger.debug(r.json.toString)
              Ok(r.json)
           })
         }
     )
   }
   
   //Display form to create profile
   def index = Action { implicit request => 
     Ok(views.html.profiles.form("Ask me ! ", profileFrom))
   }

   /*val bytesToString: Enumeratee[Array[Byte],String] = Enumeratee.map[Array[Byte]]{ byteArray => new String(byteArray)}

   def twitter(term: String) = Action { request =>
      val tokens = Twitter.sessionTokenPair(request).get
      val toComet = bytesToString ><> Comet(callback = "window.parent.twitts")(Comet.CometMessage(identity))

      Ok.stream { socket: Socket.Out[play.api.templates.Html] =>
         WS.url("https://stream.twitter.com/1/statuses/filter.json?track=" + term)
               .sign(OAuthCalculator(Twitter.KEY, tokens))
               .get(res => toComet &> socket)
      }
   }*/

}