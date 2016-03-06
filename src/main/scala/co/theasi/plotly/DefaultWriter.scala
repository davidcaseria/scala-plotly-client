package co.theasi.plotly

trait DefaultWriter extends Writer {
  val credentials = Credentials.read
  val plotlyUrl = "https://plot.ly/clientresp"
}
