name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "mysql" % "mysql-connector-java" % "5.1.18",
  "org.apache.commons" % "commons-email" % "1.4",
  "commons-net" % "commons-net" % "3.5",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "com.google.code.gson" % "gson" % "2.8.0",
  "com.paypal.sdk" % "paypal-core" % "1.7.2"
)

resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

fork in run := false

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

fork in run := true