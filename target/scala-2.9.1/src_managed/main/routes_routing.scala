// @SOURCE:/Users/jch/hackday/github/opinionz/conf/routes
// @HASH:c3efbf513045b5205a2c17aa18acde186992abf1
// @DATE:Mon Feb 20 13:13:18 CET 2012

import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString

object Routes extends Router.Routes {


// @LINE:6
val controllers_Opinions_index0 = Route("GET", PathPattern(List(StaticPart("/"))))
                    

// @LINE:7
val controllers_Opinions_eval1 = Route("POST", PathPattern(List(StaticPart("/result"))))
                    

// @LINE:10
val controllers_Assets_at2 = Route("GET", PathPattern(List(StaticPart("/assets/"),DynamicPart("file", """.+"""))))
                    
def documentation = List(("""GET""","""/""","""controllers.Opinions.index"""),("""POST""","""/result""","""controllers.Opinions.eval"""),("""GET""","""/assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)"""))
             
    
def routes:PartialFunction[RequestHeader,Handler] = {        

// @LINE:6
case controllers_Opinions_index0(params) => {
   call { 
        invokeHandler(_root_.controllers.Opinions.index, HandlerDef(this, "controllers.Opinions", "index", Nil))
   }
}
                    

// @LINE:7
case controllers_Opinions_eval1(params) => {
   call { 
        invokeHandler(_root_.controllers.Opinions.eval, HandlerDef(this, "controllers.Opinions", "eval", Nil))
   }
}
                    

// @LINE:10
case controllers_Assets_at2(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(_root_.controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String])))
   }
}
                    
}
    
}
                