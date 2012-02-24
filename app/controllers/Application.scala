package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import views._


object Application extends Controller {
   def index = Security.Authenticated(
      request => request.session.get("token"),
      _ => Redirect(routes.Twitter.authenticate()))(username => Profiles.index)

   def logout = Action { request =>
      Redirect(routes.Application.index()).withNewSession
   }

}