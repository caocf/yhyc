import sbt._
import Keys._

object ApplicationBuild extends Build {

  val appName = "yhyc"
  val appVersion = "1.0-SNAPSHOT"

  scalaVersion := "2.10.4"

  val appDependencies = Seq(
    "net.vz.mongodb.jackson" %% "play-mongo-jackson-mapper" % "1.1.0")

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases",
    resolvers += "Scala-Tools Maven2 Snapshots Repository" at "http://scala-tools.org/repo-snapshots"
      // settings
  )

}
