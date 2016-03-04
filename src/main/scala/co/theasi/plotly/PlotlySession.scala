package co.theasi.plotly

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scalaj.http.{ Http, HttpRequest, HttpResponse }

import org.json4s.JString
import org.json4s.native.JsonMethods.parse

trait Session {
  val credentials: Credentials
  val plotlyUrl: String

  def sendToPlotlyAsync(
      origin: String,
      args: String,
      kwargs: String
  ): Future[Unit] = {
    val response = Future {
      request(origin, args, kwargs).asString
    }
    response.map { processPlotlyResponse _ }
  }

  def sendToPlotly(
      origin: String,
      args: String,
      kwargs: String
  ) {
    val response = request(origin, args, kwargs).asString
    processPlotlyResponse(response)
  }

  private def request(
      origin: String,
      args: String,
      kwargs: String
    ): HttpRequest = {
    val request = Http(plotlyUrl).postForm(Seq(
      "un" -> credentials.username,
      "key" -> credentials.key,
      "origin" -> origin,
      "platform" -> "scala",
      "args" -> args,
      "kwargs" -> kwargs
    ))
    request
  }

  private def processPlotlyResponse(response: HttpResponse[String]) {
    val parsedBody = parse(response.body)
    val JString(err) = parsedBody \ "error"
    if (err != "") {
      throw new PlotlyException(err)
    }
    val JString(warning) = parsedBody \ "warning"
    if (warning != "") {
      throw new PlotlyException(warning)
    }
    val JString(message) = parsedBody \ "message"
    if (message != "") {
      throw new PlotlyException(message)
    }
  }

}

class DefaultSession extends Session {
  val credentials = Credentials.read
  val plotlyUrl = "https://plot.ly/clientresp"
}
