sonatypeProfileName := "co.theasi"

// For Maven Central
pomExtra in Global := {
  <url>https://github.com/ASIDataScience/plotly-client</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>https://opensource.org/licenses/MIT</url>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:github.com/ASIDataScience/plotly-client</connection>
    <developerConnection>scm:git:git@github.com:ASIDataScience/plotly-client</developerConnection>
    <url>github.com/ASIDataScience/plotly-client</url>
  </scm>
  <developers>
    <developer>
      <id>pbugnion</id>
      <name>Pascal Bugnion</name>
      <url>pascalbugnion.net</url>
    </developer>
  </developers>
}
