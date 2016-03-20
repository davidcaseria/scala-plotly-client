package co.theasi.plotly

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import scalaj.http._

import scala.util.{ Try, Success, Failure }

case class Plot(
  val fileName: String,
  val series: List[Series2D[PType, PType]] = List.empty,
  fileOptions: FileOptions = FileOptions()
) {

  def withScatter[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y]
  ): Plot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = Scatter(xsAsPType, ysAsPType, ScatterOptions()) :: series)
  }

  def withBar[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y]
  ): Plot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = Bar(xsAsPType, ysAsPType, BarOptions()) :: series)
  }

  def grid: Grid = {
    val columns = series.zipWithIndex.flatMap {
      case (s, index) =>
        List(s"x-$index" -> s.xs, s"y-$index" -> s.ys)
    }.toMap
    Grid(fileName+"-grid", columns, fileOptions)
  }

  def draw(implicit api: Api) {
    if (fileOptions.overwrite) {
      Try { DrawnPlot.fromFileName(fileName)(api) } match {
        case Success(plot) => // exists already -> delete
          api.despatchAndInterpret(api.delete(s"plots/${plot.fileId}"))
        case Failure(PlotlyException("Not found.")) => // good to go
        case Failure(e) => throw e // some other error -> re-throw
      }
    }
    val drawnGrid = grid.draw(api)
    val seriesAsJson = series.zipWithIndex.map { case (series, index) =>
      val srcs = srcsFromDrawnGrid(drawnGrid, index)
      SeriesWriter.toJson(srcs, series.options)
    }
    val body = (
      ("figure" -> ("data" -> seriesAsJson)) ~
      ("filename" -> fileName) ~
      ("world_readable" -> true)
    )
    val request = api.post("plots", compact(render(body)))
    val responseAsJson = api.despatchAndInterpret(request)
    DrawnPlot.fromResponse(responseAsJson \ "file")
  }

  private def srcsFromDrawnGrid(drawnGrid: DrawnGrid, index: Int)
  : List[String] = {
    val xName = s"x-$index"
    val yName = s"y-$index"
    val xuid = drawnGrid.columnUids(xName)
    val yuid = drawnGrid.columnUids(yName)
    val xsrc = s"${drawnGrid.fileId}:$xuid"
    val ysrc = s"${drawnGrid.fileId}:$yuid"
    List(xsrc, ysrc)
  }
}
