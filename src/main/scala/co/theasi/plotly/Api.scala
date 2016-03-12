package co.theasi.plotly

import scalaj.http._

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

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

  def get(endPoint: String, params: Seq[(String, String)]): HttpRequest = {
    get(endPoint).params(params)
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

  def delete(endPoint: String): HttpRequest = {
    get(endPoint).method("DELETE")
  }

  def despatchAndInterpret(request: HttpRequest): JValue = {
    val response = request.asString
    if (!response.is2xx) {
      val responseBody = parse(response.body)
      val JString(msg) = responseBody \ "detail"
      throw new PlotlyException(msg)
    }
    else {
      parse(response.body)
    }
  }

}
