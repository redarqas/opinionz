package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._


import models.Criterion

object Application extends Controller {
  
  val opinionForm: Form[Criterion] = Form(
      mapping(
        "text" -> text
      ) {
        (text) => Criterion(text)
      }
      {
        criterion => Some(criterion.text)
      }
   )
  
   
   
  def index = Action {
    Ok(views.html.index("Your new application is ready.", opinionForm))
  }
  
}