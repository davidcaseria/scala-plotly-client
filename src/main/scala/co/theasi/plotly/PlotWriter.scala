package co.theasi.plotly

import scala.concurrent.Future

object PlotWriter {

  def plot(
      x: Iterable[Double],
      y: Iterable[Double],
      fileName: String)(
      implicit  session: Session)
  : Response = {
    val args = s""" [ [${x.mkString(",")}], [${y.mkString(",")}] ] """
    val origin = "plot"
    val kwargs = s""" { "filename": "$fileName" } """
    session.sendToPlotly(origin, args, kwargs)
  }

  def plotAsync(
      x: Iterable[Double],
      y: Iterable[Double],
      fileName: String)(
      implicit session: Session)
  : Future[Response] = {
    val args = s""" [ [${x.mkString(",")}], [${y.mkString(",")}] ] """
    val origin = "plot"
    val kwargs = s""" { "filename": "$fileName" } """
    session.sendToPlotlyAsync(origin, args, kwargs)
  }

}
