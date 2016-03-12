package co.theasi.plotly

import scalaj.http._

trait ApiWithDefaultCredentials extends Api {
  override lazy val credentials = Credentials.read
}

trait Api {

  def credentials: Credentials
  def url = "https://api.plot.ly/v2/"

  def get(endPoint: String): HttpRequest = {
    val request = Http(url + endPoint.stripPrefix("/"))
      .auth(credentials.username, credentials.key)
      .headers(Seq(
        "Plotly-Client-Platform" -> "scala",
        "Accept" -> "application/json"
      ))
    request
  }

  def post(endPoint: String, body: String): HttpRequest = {
    val request = Http(url + endPoint.stripPrefix("/"))
      .auth(credentials.username, credentials.key)
      .headers(Seq(
        "Plotly-Client-Platform" -> "scala",
        "Accept" -> "application/json",
        "Content-Type" -> "application/json"
      ))
      .postData(body)
    request
  }

}
