import sbt._
import sbt.Keys._

object UncleverBuild extends Build {

  lazy val unclever = Project(id = "unclever", base = file("."))
    .configs(IntegrationTest)
    .settings(
      scalaVersion := "2.11.7",
      scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings"),
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "2.2.4" % "it,test",
        "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "it,test",
        "com.h2database" % "h2" % "1.4.187" % "it,test"))

}
