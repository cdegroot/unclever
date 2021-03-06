import sbt._
import sbt.Keys._

object UncleverBuild extends Build {

  lazy val root = Project(id = "unclever", base = file("."))
    .configs(IntegrationTest)
    .settings(
      scalaVersion := "2.11.7",
      crossScalaVersions := Seq("2.10.5", "2.11.7"),
      scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings"),
      libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "2.2.4" % "test",
        "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
        "com.h2database" % "h2" % "1.4.187" % "test"))

}
