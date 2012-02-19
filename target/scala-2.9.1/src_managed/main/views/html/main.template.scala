
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
object main extends BaseScalaTemplate[play.api.templates.Html,Format[play.api.templates.Html]](play.api.templates.HtmlFormat) with play.api.templates.Template2[String,Html,play.api.templates.Html] {

    /**/
    def apply/*1.2*/(title: String)(content: Html):play.api.templates.Html = {
        _display_ {

Seq(format.raw/*1.32*/("""

<!DOCTYPE html>

<html>
    <head>
        <title>"""),_display_(Seq(/*7.17*/title)),format.raw/*7.22*/("""</title>
        <link rel="stylesheet" media="screen" href=""""),_display_(Seq(/*8.54*/routes/*8.60*/.Assets.at("stylesheets/main.css"))),format.raw/*8.94*/("""">
        <link rel="shortcut icon" type="image/png" href=""""),_display_(Seq(/*9.59*/routes/*9.65*/.Assets.at("images/favicon.png"))),format.raw/*9.97*/("""">
        <script src=""""),_display_(Seq(/*10.23*/routes/*10.29*/.Assets.at("javascripts/jquery-1.7.1.min.js"))),format.raw/*10.74*/("""" type="text/javascript"></script>
    </head>
    <body>
        """),_display_(Seq(/*13.10*/content)),format.raw/*13.17*/("""
    </body>
</html>
"""))}
    }
    
    def render(title:String,content:Html) = apply(title)(content)
    
    def f:((String) => (Html) => play.api.templates.Html) = (title) => (content) => apply(title)(content)
    
    def ref = this

}
                /*
                    -- GENERATED --
                    DATE: Sun Feb 19 21:53:18 CET 2012
                    SOURCE: /Users/jch/hackday/github/opinionz/app/views/main.scala.html
                    HASH: 6a9d3e71a4db67b23442aa0e15837c7026dc07bb
                    MATRIX: 509->1|611->31|694->84|720->89|812->151|826->157|881->191|972->252|986->258|1039->290|1095->315|1110->321|1177->366|1275->433|1304->440
                    LINES: 19->1|22->1|28->7|28->7|29->8|29->8|29->8|30->9|30->9|30->9|31->10|31->10|31->10|34->13|34->13
                    -- GENERATED --
                */
            