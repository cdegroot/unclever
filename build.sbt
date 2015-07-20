name := """unclever"""

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"

// Shapeless *might* solve some design issues. Later.
// libraryDependencies += "com.chuusai" %% "shapeless" % "2.2.4"

// For integration testing
libraryDependencies += "com.h2database" % "h2" % "1.4.187" % "test"

