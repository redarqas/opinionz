import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "opinionz"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "com.novus" %% "salat-core" % "0.0.8-SNAPSHOT"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA)
				.settings(resolvers ++= Seq(DefaultMavenRepository, "repo.novus snaps" at "http://repo.novus.com/snapshots/"))
}
