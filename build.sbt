name := "hello-play-scala"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"



//resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

//https://repo1.maven.org/maven2/org/reactivemongo/play2-reactivemongo_2.11/0.11.5.play24/play2-reactivemongo_2.11-0.11.5.play24.pom

// sonatype did not have the 0.11.5.play24 repo available.
// hit MAVEN direct instead and found them.
//resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += "Maven" at "https://repo1.maven.org/maven2"


lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  cache,
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "3.3.4",
  "org.webjars" % "angularjs" % "1.3.0-beta.2",
  "org.webjars" % "requirejs" % "2.1.11-1",

  "org.reactivemongo" %% "reactivemongo" % "0.11.5",
  "org.reactivemongo" % "play2-reactivemongo_2.11" % "0.11.5.play24"
)



scalariformSettings
