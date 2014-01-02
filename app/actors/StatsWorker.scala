package actors

import akka.actor._
import akka.actor.Actor._
import play.api.libs.concurrent._
import play.api.Play.current
import play.libs.Akka
import services.Statistics
import play.api.Logger
import actors.StatsWorker._
import models.{ Profile, Tweet }

class StatsWorker extends Actor {
  def receive = {
    case g: GlobalComputation => {
     // Profile.all.map(Tweet.incMapReduce(_))
      Logger.debug("Global stats computation hook over")
    }
    case d: DailyComputation => {
      //Logger.debug("Global stats computation hook over")
    }
    case w: WeeklyComputation => {
      //Logger.debug("Global stats computation hook over")
    }
    case _ => throw new MatchError("Nothing matched")
  }
}

object StatsWorker {
  lazy val ref = Akka.system.actorOf(Props[StatsWorker])
  sealed trait Event
  case class GlobalComputation() extends Event
  case class DailyComputation() extends Event
  case class WeeklyComputation() extends Event
}
