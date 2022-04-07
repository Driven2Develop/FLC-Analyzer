name := """flcAnalyzer"""
organization := "com.freelancelot"

version := "1.0.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

// Works: scala 2.12, sbt 1.6.2, Homebrew Java 17.0.2
scalaVersion := "2.12.2"
lazy val akkaVersion = "2.6.18"
lazy val akkaHttpVersion = "10.2.9"


libraryDependencies ++= Seq(
  javaWs,
  guice,
  caffeine,
  "org.mockito" % "mockito-core" % "4.4.0" % Test,
  "org.assertj" % "assertj-core" % "3.22.0" % Test,
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test
)

