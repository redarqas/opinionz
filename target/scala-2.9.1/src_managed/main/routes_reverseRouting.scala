// @SOURCE:/Users/jch/hackday/github/opinionz/conf/routes
// @HASH:506c93bfaacc37422a23c1c13bb7e03704080978
// @DATE:Mon Feb 20 12:02:34 CET 2012

import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._


import Router.queryString


// @LINE:10
// @LINE:7
// @LINE:6
package controllers {

// @LINE:7
// @LINE:6
class ReverseOpinions {
    


 
// @LINE:6
def index() = {
   Call("GET", "/")
}
                                                        
 
// @LINE:7
def eval() = {
   Call("POST", "/")
}
                                                        

                      
    
}
                            

// @LINE:10
class ReverseAssets {
    


 
// @LINE:10
def at(file:String) = {
   Call("GET", "/assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                        

                      
    
}
                            
}
                    


// @LINE:10
// @LINE:7
// @LINE:6
package controllers.javascript {

// @LINE:7
// @LINE:6
class ReverseOpinions {
    


 
// @LINE:6
def index = JavascriptReverseRoute(
   "controllers.Opinions.index",
   """
      function() {
      return _wA({method:"GET", url:"/"})
      }
   """
)
                                                        
 
// @LINE:7
def eval = JavascriptReverseRoute(
   "controllers.Opinions.eval",
   """
      function() {
      return _wA({method:"POST", url:"/"})
      }
   """
)
                                                        

                      
    
}
                            

// @LINE:10
class ReverseAssets {
    


 
// @LINE:10
def at = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"/assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                                                        

                      
    
}
                            
}
                    


// @LINE:10
// @LINE:7
// @LINE:6
package controllers.ref {

// @LINE:7
// @LINE:6
class ReverseOpinions {
    


 
// @LINE:6
def index() = new play.api.mvc.HandlerRef(
   controllers.Opinions.index(), HandlerDef(this, "controllers.Opinions", "index", Seq())
)
                              
 
// @LINE:7
def eval() = new play.api.mvc.HandlerRef(
   controllers.Opinions.eval(), HandlerDef(this, "controllers.Opinions", "eval", Seq())
)
                              

                      
    
}
                            

// @LINE:10
class ReverseAssets {
    


 
// @LINE:10
def at(path:String, file:String) = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]))
)
                              

                      
    
}
                            
}
                    
                