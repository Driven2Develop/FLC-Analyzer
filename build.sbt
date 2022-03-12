name := """flcAnalyzer"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.6"

libraryDependencies += guice

libraryDependencies ++= Seq(
  javaWs
)

libraryDependencies += "org.mockito" % "mockito-core" % "4.4.0" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.22.0" % Test

