
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
object opinion extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template1[Opinion,play.api.templates.Html] {

    /**/
    def apply/*1.2*/(opinion: Opinion):play.api.templates.Html = {
        _display_ {

Seq(format.raw/*1.20*/("""

"""),_display_(Seq(/*3.2*/main("Welcome to Play 2.0")/*3.29*/ {_display_(Seq(format.raw/*3.31*/("""
  """),_display_(Seq(/*4.4*/opinion/*4.11*/.mood)),format.raw/*4.16*/("""
""")))})))}
    }
    
    def render(opinion:Opinion) = apply(opinion)
    
    def f:((Opinion) => play.api.templates.Html) = (opinion) => apply(opinion)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Mon Feb 20 12:17:18 CET 2012
                    SOURCE: /Users/jch/hackday/github/opinionz/app/views/opinions/opinion.scala.html
                    HASH: 7c1a83c207dde623ba336eb8fabf70014f69c6a4
                    MATRIX: 517->1|607->19|639->22|674->49|708->51|741->55|756->62|782->67
                    LINES: 19->1|22->1|24->3|24->3|24->3|25->4|25->4|25->4
                    -- GENERATED --
                */
            