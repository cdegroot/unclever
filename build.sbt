name := """unclever"""

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"

// For integration testing
libraryDependencies += "com.h2database" % "h2" % "1.4.187" % "test"

