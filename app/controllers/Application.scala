package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._
import views._
import security.Secured._

object Application extends Controller {
  //Logout from application
  def logout = Action { request =>
    Redirect(routes.Profiles.index).withNewSession
  }

  def index = Authenticated { implicit request =>
    Redirect(routes.Profiles.index)
  }

}