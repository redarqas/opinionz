
package views.html.opinions

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import play.api.i18n._
import play.api.mvc._
import play.api.data._
import views.html._
/**/
object form extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template2[String,Form[String],play.api.templates.Html] {

    /**/
    def apply/*1.2*/(message: String, opinionForm: Form[String]):play.api.templates.Html = {
        _display_ {import helper._


Seq(format.raw/*1.46*/("""

"""),format.raw/*4.1*/("""
"""),_display_(Seq(/*5.2*/main("Welcome to Play 2.0")/*5.29*/ {_display_(Seq(format.raw/*5.31*/("""
    
   """),_display_(Seq(/*7.5*/helper/*7.11*/.form(action = routes.Opinions.eval)/*7.47*/ {_display_(Seq(format.raw/*7.49*/("""
     <fieldset>
         <legend>Opinion Finder</legend>
         """),_display_(Seq(/*10.11*/inputText(
                opinionForm("text"), 
                '_label -> "Your sentence", 
                '_error -> opinionForm.globalError
         ))),format.raw/*14.11*/("""
     </fieldset>
     <div class="actions">
         <input type="submit" class="btn primary" value="Eval">
         <a href=""""),_display_(Seq(/*18.20*/routes/*18.26*/.Opinions.index)),format.raw/*18.41*/("""" class="btn">Cancel</a>
     </div>
   """)))})),format.raw/*20.5*/("""
""")))})),format.raw/*21.2*/("""
"""))}
    }
    
    def render(message:String,opinionForm:Form[String]) = apply(message,opinionForm)
    
    def f:((String,Form[String]) => play.api.templates.Html) = (message,opinionForm) => apply(message,opinionForm)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Tue Feb 21 21:47:53 CET 2012
                    SOURCE: /Users/alabbe/Dev/zenexity_workspace/hackdays/opinionz/app/views/opinions/form.scala.html
                    HASH: 6bbfd6ed8c184329a514b488a2514c307246a48d
                    MATRIX: 526->1|658->45|686->64|717->66|752->93|786->95|825->105|839->111|883->147|917->149|1016->217|1193->372|1352->500|1367->506|1404->521|1476->562|1509->564
                    LINES: 19->1|23->1|25->4|26->5|26->5|26->5|28->7|28->7|28->7|28->7|31->10|35->14|39->18|39->18|39->18|41->20|42->21
                    -- GENERATED --
                */
            