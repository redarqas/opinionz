import actors.StatsWorker
import actors.StatsWorker._
import play.api._
import play.libs.Akka
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.language.postfixOps

object Global extends GlobalSettings {
  override def onStart(app: Application) {

    //incremental statistics computation
    Akka.system.scheduler.schedule(3 seconds, 60 seconds) {
      StatsWorker.ref ! GlobalComputation()
    }
    Akka.system.scheduler.schedule(3 seconds, 1 day) {
      StatsWorker.ref ! DailyComputation()
    }
    Akka.system.scheduler.schedule(3 seconds, 7 days) {
      StatsWorker.ref ! WeeklyComputation()
    }

  }
}
