import play.api._

import models._
import anorm._

object Global extends GlobalSettings {

   override def onStart(app: Application) {
      InitialData.insert()
   }

}

/**
 * Initial set of data to be imported
 * in the sample application.
 */
object InitialData {

   def insert() = {
      /*if(User.count() < 1) {
         Logger.debug("Bootstraping initial data")

         Seq(
            User(email = "Guillaume Bort"),
            User(email="Maxime Dantec"),
            User(email="Sadek Drobi"),
            User(email="Erwan Loisant")
         ).map(User.insert(_))

      } else {
         //Logger.debug("Database contains: %s User(s) and %s TrainingSession(s)".format(User.count(),TrainingSession.count()))
         Logger.debug("Did not bootstrap any data, drop the database to do so for next startup")
      }*/

   }

}