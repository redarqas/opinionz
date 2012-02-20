
package views.html

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
object index extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template2[String,Form[Criterion],play.api.templates.Html] {

    /**/
    def apply/*1.2*/(message: String, opinionForm: Form[Criterion]):play.api.templates.Html = {
        _display_ {

Seq(format.raw/*1.49*/("""

"""),_display_(Seq(/*3.2*/main("Welcome to Play 2.0")/*3.29*/ {_display_(Seq(format.raw/*3.31*/("""
    
    """),_display_(Seq(/*5.6*/play20/*5.12*/.welcome(message))),format.raw/*5.29*/("""
    
""")))})),format.raw/*7.2*/("""
"""))}
    }
    
    def render(message:String,opinionForm:Form[Criterion]) = apply(message,opinionForm)
    
    def f:((String,Form[Criterion]) => play.api.templates.Html) = (message,opinionForm) => apply(message,opinionForm)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Mon Feb 20 11:45:06 CET 2012
                    SOURCE: /Users/jch/hackday/github/opinionz/app/views/index.scala.html
                    HASH: 2f3c7049eea297b86e2172f10897da57ebdbe802
                    MATRIX: 521->1|640->48|672->51|707->78|741->80|781->91|795->97|833->114|870->121
                    LINES: 19->1|22->1|24->3|24->3|24->3|26->5|26->5|26->5|28->7
                    -- GENERATED --
                */
            