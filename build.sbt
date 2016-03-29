name := "plotly-client"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

crossScalaVersions := Seq("2.11.7", "2.10.4")

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "2.2.1",
  "org.json4s" %% "json4s-native" % "3.3.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

initialCommands := """
  |import co.theasi.plotly._
""".stripMargin

publishMavenStyle := true

parallelExecution in Test := false

logBuffered in Test := false

// Publishing not enabled yet.
publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
