/**
 * User: alabbe
 * Date: 30/03/12
 * Time: 16:02
 */

import actors.StatsWorker
import actors.StatsWorker._
import java.util.Date
import models._
import play.api._

import anorm._
import scala.actors.Scheduler
import akka.util.duration._
import play.libs.Akka

object Global extends GlobalSettings {

   override def onStart(app: Application) {

      //incremental statistics computation
      Akka.system.scheduler.schedule(3 seconds, 60 seconds) {
         StatsWorker.ref ! GlobalComputation()
      }
      Akka.system.scheduler.schedule(3 seconds, 1 day) {
         StatsWorker.ref ! DailyComputation()
      }
      Akka.system.scheduler.schedule(3 seconds, 7 days ) {
         StatsWorker.ref ! WeeklyComputation()
      }


   }
}
