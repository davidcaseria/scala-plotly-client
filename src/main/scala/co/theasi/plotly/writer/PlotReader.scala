package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._

import co.theasi.plotly.Plot

object PlotReader {
  def fetch(file: PlotFile)(implicit server: Server): Plot = {
    val request = Api.get(
      s"plots/${file.fileId}/content", Seq("inline_data" -> "true"))
    val response = Api.despatchAndInterpret(request)
    fromResponse(response)
  }

  def fromResponse(response: JValue): Plot = {
    val JArray(dataAsJson) = (response \ "data")
    val plotSeries = dataAsJson.map {
      case series: JObject => SeriesReader.fromJson(series)
      case r => throw new UnexpectedServerResponse(
        s"Bad response from server for Plot object: $r")
    }
    new Plot(plotSeries)
  }
}
