
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
  <ul>
    <li>"""),_display_(Seq(/*5.10*/opinion/*5.17*/.text)),format.raw/*5.22*/("""</li>
    <li>"""),_display_(Seq(/*6.10*/opinion/*6.17*/.mood)),format.raw/*6.22*/("""</li>
    <li>"""),_display_(Seq(/*7.10*/opinion/*7.17*/.prob)),format.raw/*7.22*/("""</li>
  </ul>
""")))})))}
    }
    
    def render(opinion:Opinion) = apply(opinion)
    
    def f:((Opinion) => play.api.templates.Html) = (opinion) => apply(opinion)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Tue Feb 21 21:47:53 CET 2012
                    SOURCE: /Users/alabbe/Dev/zenexity_workspace/hackdays/opinionz/app/views/opinions/opinion.scala.html
                    HASH: 45bb16cdaeea49f05bab83471e69d614c588d111
                    MATRIX: 517->1|607->19|639->22|674->49|708->51|755->68|770->75|796->80|841->95|856->102|882->107|927->122|942->129|968->134
                    LINES: 19->1|22->1|24->3|24->3|24->3|26->5|26->5|26->5|27->6|27->6|27->6|28->7|28->7|28->7
                    -- GENERATED --
                */
            