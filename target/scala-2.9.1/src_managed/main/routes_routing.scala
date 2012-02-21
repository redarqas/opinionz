// @SOURCE:/Users/alabbe/Dev/zenexity_workspace/hackdays/opinionz/conf/routes
// @HASH:f3d8fa6348c2e69d9089bbd2e700b467056d0120
// @DATE:Tue Feb 21 22:04:43 CET 2012

import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString

object Routes extends Router.Routes {


// @LINE:7
val controllers_Application_index0 = Route("GET", PathPattern(List(StaticPart("/"))))
                    

// @LINE:8
val controllers_Application_logout1 = Route("GET", PathPattern(List(StaticPart("/logout"))))
                    

// @LINE:9
val controllers_Twitter_authenticate2 = Route("GET", PathPattern(List(StaticPart("/auth"))))
                    

// @LINE:10
val controllers_Opinions_index3 = Route("GET", PathPattern(List(StaticPart("/search"))))
                    

// @LINE:11
val controllers_Opinions_eval4 = Route("POST", PathPattern(List(StaticPart("/result"))))
                    

// @LINE:14
val controllers_Assets_at5 = Route("GET", PathPattern(List(StaticPart("/assets/"),DynamicPart("file", """.+"""))))
                    
def documentation = List(("""GET""","""/""","""controllers.Application.index"""),("""GET""","""/logout""","""controllers.Application.logout"""),("""GET""","""/auth""","""controllers.Twitter.authenticate"""),("""GET""","""/search""","""controllers.Opinions.index"""),("""POST""","""/result""","""controllers.Opinions.eval"""),("""GET""","""/assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)"""))
             
    
def routes:PartialFunction[RequestHeader,Handler] = {        

// @LINE:7
case controllers_Application_index0(params) => {
   call { 
        invokeHandler(_root_.controllers.Application.index, HandlerDef(this, "controllers.Application", "index", Nil))
   }
}
                    

// @LINE:8
case controllers_Application_logout1(params) => {
   call { 
        invokeHandler(_root_.controllers.Application.logout, HandlerDef(this, "controllers.Application", "logout", Nil))
   }
}
                    

// @LINE:9
case controllers_Twitter_authenticate2(params) => {
   call { 
        invokeHandler(_root_.controllers.Twitter.authenticate, HandlerDef(this, "controllers.Twitter", "authenticate", Nil))
   }
}
                    

// @LINE:10
case controllers_Opinions_index3(params) => {
   call { 
        invokeHandler(_root_.controllers.Opinions.index, HandlerDef(this, "controllers.Opinions", "index", Nil))
   }
}
                    

// @LINE:11
case controllers_Opinions_eval4(params) => {
   call { 
        invokeHandler(_root_.controllers.Opinions.eval, HandlerDef(this, "controllers.Opinions", "eval", Nil))
   }
}
                    

// @LINE:14
case controllers_Assets_at5(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(_root_.controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String])))
   }
}
                    
}
    
}
                