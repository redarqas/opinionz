import sbt._
import Keys._

object BuildSettings {
  val buildOrganization = "jamal"
  val buildVersion      = "1.0"
  val buildScalaVersion = "2.10.3"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion
  )
}


object Resolvers {
  val sunrepo    = "Sun Maven2 Repo" at "http://download.java.net/maven/2"
  val sunrepoGF  = "Sun GF Maven2 Repo" at "http://download.java.net/maven/glassfish"
  val oraclerepo = "Oracle Maven2 Repo" at "http://download.oracle.com/maven"
  val novusRepo  = "repo.novus snaps" at "http://repo.novus.com/snapshots/"

  val oracleResolvers = Seq (sunrepo, sunrepoGF, oraclerepo, novusRepo)
}

object Dependencies {
 val scalatest = "org.scalatest" % "scalatest_2.10" % "2.0" % "test"
 val salat = "com.novus" %% "salat" % "1.9.4"
}

object AppBuild extends Build {
  import Resolvers._
  import Dependencies._
  import BuildSettings._

  // Sub-project specific dependencies
  val commonDeps = Seq (
    scalatest,
    salat
  )
  
  lazy val app = Project (
    "opinionz",
    file ("."),
    settings = buildSettings ++ Seq (libraryDependencies ++= commonDeps)
  )
}
