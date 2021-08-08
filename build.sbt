import Dependencies._

ThisBuild / scalaVersion     := "2.13.6"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "org.cmhh"
ThisBuild / organizationName := "cmhh"

scalacOptions ++= Seq("-deprecation", "-feature")

lazy val root = (project in file("."))
  .settings(
    name := "akkatrain",

    libraryDependencies ++= Seq(
      akkaactor,
      dl4jcore,
      nd4j,
      slf4japi,
      slf4jlog4j,
      log4j
    ), 
    
    ThisBuild / assemblyMergeStrategy := {
      case n if n.contains("services") => MergeStrategy.concat
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    },

    assembly / assemblyJarName := "akkatrain.jar"
  )
