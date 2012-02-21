package controllers

import models._
import services._
import play.Logger
import play.api._
import play.api.libs._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.ws._
import play.api.libs.oauth._
import play.api.libs.concurrent._
import play.api.libs.iteratee._


object Opinions extends Controller {
  
  val stringForm: Form[String] = Form("text" -> text)

   def index = Action { implicit request => 
     Ok(views.html.opinions.form("Ask me ! ", stringForm))
   }

   def eval = Action {  implicit request => 
     stringForm.bindFromRequest.fold(
      errors =>  Ok(views.html.opinions.form("Ask me ! ", errors)),
      // We got a valid User value, display the summary
      criterion => {
         val opinion = Sentiment.getSentiment(criterion)
         Ok(views.html.opinions.opinion(opinion))
       }
     )
   }

   val bytesToString: Enumeratee[Array[Byte],String] = Enumeratee.map[Array[Byte]]{ byteArray => new String(byteArray)}

   def twitter(term: String) = Action { request =>
      val tokens = Twitter.sessionTokenPair(request).get
      val toComet = bytesToString ><> Comet(callback = "window.parent.twitts")(Comet.CometMessage(identity))

      Ok.stream { socket: Socket.Out[play.api.templates.Html] =>
         WS.url("https://stream.twitter.com/1/statuses/filter.json?track=" + term)
               .sign(OAuthCalculator(Twitter.KEY, tokens))
               .get(res => toComet &> socket)
      }
   }
}