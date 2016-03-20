package co.theasi.plotly.writer

case object ServerWithDefaultCredentials extends Server {
  override lazy val credentials = Credentials.read
  val url = "https://api.plot.ly/v2/"
}

trait Server {
  def credentials: Credentials
  def url: String
}
