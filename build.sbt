//import play.Project._

name := "hello-play-scala"

version := "1.0-SNAPSHOT"

//scalaVersion := "2.10.5"
scalaVersion := "2.11.6"

//resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"


//resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

//https://repo1.maven.org/maven2/org/reactivemongo/play2-reactivemongo_2.11/0.11.5.play24/play2-reactivemongo_2.11-0.11.5.play24.pom


//resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += "Maven" at "https://repo1.maven.org/maven2"


lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "3.3.4",
  "org.webjars" % "angularjs" % "1.3.0-beta.2",
  "org.webjars" % "requirejs" % "2.1.11-1",

  // these two weren't working, but this was still the sonatype lib(s)
  //"org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
  //"org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23"

  // these two were bullshit since Sonatype doesn't appear to have the 11.5 or 11.1 version of play24.
  // bullshit.
  //"org.reactivemongo" %% "reactivemongo" % "0.11.1.play24-SNAPSHOT",
  //"org.reactivemongo" %% "play2-reactivemongo" % "0.11.1.play24-SNAPSHOT"

  "org.reactivemongo" %% "reactivemongo" % "0.11.5",
  "org.reactivemongo" % "play2-reactivemongo_2.11" % "0.11.5.play24"
)

//playScalaSettings

scalariformSettings
