name := "plotly"

version := "0.1-SNAPSHOT"

organization := "co.theasi"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.11.7", "2.10.6")

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "2.2.1",
  "org.json4s" %% "json4s-native" % "3.3.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

initialCommands := """
  |import co.theasi.plotly._
""".stripMargin

publishMavenStyle := true

// Publishing
publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

// Testing
parallelExecution in Test := false

logBuffered in Test := false


// Documentation
enablePlugins(SiteScaladocPlugin)

ghpages.settings

git.remoteRepo := "git@github.com:the-asi/scala-plotly-client.git"
