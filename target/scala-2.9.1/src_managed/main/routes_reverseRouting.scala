// @SOURCE:/Users/alabbe/Dev/zenexity_workspace/hackdays/opinionz/conf/routes
// @HASH:f3d8fa6348c2e69d9089bbd2e700b467056d0120
// @DATE:Tue Feb 21 22:04:43 CET 2012

import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString


// @LINE:14
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
package controllers {

// @LINE:11
// @LINE:10
class ReverseOpinions {
    


 
// @LINE:10
def index() = {
   Call("GET", "/search")
}
                                                        
 
// @LINE:11
def eval() = {
   Call("POST", "/result")
}
                                                        

                      
    
}
                            

// @LINE:8
// @LINE:7
class ReverseApplication {
    


 
// @LINE:8
def logout() = {
   Call("GET", "/logout")
}
                                                        
 
// @LINE:7
def index() = {
   Call("GET", "/")
}
                                                        

                      
    
}
                            

// @LINE:14
class ReverseAssets {
    


 
// @LINE:14
def at(file:String) = {
   Call("GET", "/assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                        

                      
    
}
                            

// @LINE:9
class ReverseTwitter {
    


 
// @LINE:9
def authenticate() = {
   Call("GET", "/auth")
}
                                                        

                      
    
}
                            
}
                    


// @LINE:14
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
package controllers.javascript {

// @LINE:11
// @LINE:10
class ReverseOpinions {
    


 
// @LINE:10
def index = JavascriptReverseRoute(
   "controllers.Opinions.index",
   """
      function() {
      return _wA({method:"GET", url:"/search"})
      }
   """
)
                                                        
 
// @LINE:11
def eval = JavascriptReverseRoute(
   "controllers.Opinions.eval",
   """
      function() {
      return _wA({method:"POST", url:"/result"})
      }
   """
)
                                                        

                      
    
}
                            

// @LINE:8
// @LINE:7
class ReverseApplication {
    


 
// @LINE:8
def logout = JavascriptReverseRoute(
   "controllers.Application.logout",
   """
      function() {
      return _wA({method:"GET", url:"/logout"})
      }
   """
)
                                                        
 
// @LINE:7
def index = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"/"})
      }
   """
)
                                                        

                      
    
}
                            

// @LINE:14
class ReverseAssets {
    


 
// @LINE:14
def at = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"/assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                                                        

                      
    
}
                            

// @LINE:9
class ReverseTwitter {
    


 
// @LINE:9
def authenticate = JavascriptReverseRoute(
   "controllers.Twitter.authenticate",
   """
      function() {
      return _wA({method:"GET", url:"/auth"})
      }
   """
)
                                                        

                      
    
}
                            
}
                    


// @LINE:14
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:8
// @LINE:7
package controllers.ref {

// @LINE:11
// @LINE:10
class ReverseOpinions {
    


 
// @LINE:10
def index() = new play.api.mvc.HandlerRef(
   controllers.Opinions.index(), HandlerDef(this, "controllers.Opinions", "index", Seq())
)
                              
 
// @LINE:11
def eval() = new play.api.mvc.HandlerRef(
   controllers.Opinions.eval(), HandlerDef(this, "controllers.Opinions", "eval", Seq())
)
                              

                      
    
}
                            

// @LINE:8
// @LINE:7
class ReverseApplication {
    


 
// @LINE:8
def logout() = new play.api.mvc.HandlerRef(
   controllers.Application.logout(), HandlerDef(this, "controllers.Application", "logout", Seq())
)
                              
 
// @LINE:7
def index() = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Seq())
)
                              

                      
    
}
                            

// @LINE:14
class ReverseAssets {
    


 
// @LINE:14
def at(path:String, file:String) = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]))
)
                              

                      
    
}
                            

// @LINE:9
class ReverseTwitter {
    


 
// @LINE:9
def authenticate() = new play.api.mvc.HandlerRef(
   controllers.Twitter.authenticate(), HandlerDef(this, "controllers.Twitter", "authenticate", Seq())
)
                              

                      
    
}
                            
}
                    
                