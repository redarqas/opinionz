package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import views._
import play.api.libs.ws._
import play.api.libs.oauth.OAuthCalculator

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

}