package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import views._


object Application extends Controller {
  //Root of application : insure authentication from twitter 
   def index = Security.Authenticated(
      request => request.session.get("token"),
      //Redirect to Twitter Oauth1
      _ => Redirect(routes.Twitter.authenticate()))(username => Profiles.index)
   //Logout from application
   def logout = Action { request =>
      Redirect(routes.Application.index()).withNewSession
   }

}