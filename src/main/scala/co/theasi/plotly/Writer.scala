package co.theasi.plotly

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scalaj.http.{ Http, HttpRequest, HttpResponse }

import org.json4s.JString
import org.json4s.native.JsonMethods.parse

trait Writer {
  val credentials: Credentials
  val plotlyUrl: String
  private def plotlyEndpoint = plotlyUrl + "clientresp"

  def plot[X: Writable, Y: Writable](
      x: Iterable[X],
      y: Iterable[Y],
      fileName: String)
  : Response = {
    val args = s""" [ [${x.mkString(",")}], [${y.mkString(",")}] ] """
    val origin = "plot"
    val kwargs = s""" { "filename": "$fileName" } """
    sendToPlotly(origin, args, kwargs)
  }

  private def sendToPlotly(
      origin: String,
      args: String,
      kwargs: String
  ): Response = {
    val response = request(origin, args, kwargs).asString
    processPlotlyResponse(response)
  }

  private def request(
      origin: String,
      args: String,
      kwargs: String
    ): HttpRequest = {
    val request = Http(plotlyEndpoint).postForm(Seq(
      "un" -> credentials.username,
      "key" -> credentials.key,
      "origin" -> origin,
      "platform" -> "scala",
      "args" -> args,
      "kwargs" -> kwargs
    ))
    request
  }

  private def processPlotlyResponse(response: HttpResponse[String]): Response = {
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
    val JString(url) = parsedBody \ "url"
    Response(url)
  }

}
