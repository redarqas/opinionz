package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models._


import models.Criterion

object Opinions extends Controller {
  
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

   def index = Action { implicit request => 
     Ok(views.html.opinions.form("Ask me ! ", opinionForm))
   }

   def eval = Action {  implicit request => 
     opinionForm.bindFromRequest.fold(
      errors =>  Ok(views.html.opinions.form("Ask me ! ", errors)),
      // We got a valid User value, display the summary
      criterion => {
         val opinion = services.Sentiment.getSentiment(criterion.text)
         Ok(views.html.opinions.opinion(opinion))
       }
     )
   }
}