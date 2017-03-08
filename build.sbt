name := "memcached-ratelimiter"

version := "1.0"

scalaVersion := "2.11.7"


scalacOptions ++=  Seq(
  "-deprecation",
  "-unchecked",
  "-feature"
)

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "net.spy" % "spymemcached" % "2.12.0",
  "com.google.inject" % "guice" % "3.0",
  "org.specs2" %% "specs2-core" % "3.6.5" % "test",
  "org.specs2" % "specs2-mock_2.11" % "3.6.5-20151026032022-1930bd6"
)


scalariformSettings
