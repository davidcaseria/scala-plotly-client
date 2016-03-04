package co.theasi.plotly

import scala.concurrent.Future

object PlotWriter extends WriterBase {

  def plot(x: Iterable[Double], y: Iterable[Double], fileName: String) {
    val args = s""" [ [${x.mkString(",")}], [${y.mkString(",")}] ] """
    val origin = "plot"
    val kwargs = s""" { "filename": "$fileName" } """
    sendToPlotly(origin, args, kwargs)
  }

  def plotAsync(x: Iterable[Double], y: Iterable[Double], fileName: String): Future[Unit] = {
    val args = s""" [ [${x.mkString(",")}], [${y.mkString(",")}] ] """
    val origin = "plot"
    val kwargs = s""" { "filename": "$fileName" } """
    sendToPlotlyAsync(origin, args, kwargs)
  }

}
