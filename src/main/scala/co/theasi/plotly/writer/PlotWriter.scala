package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import scalaj.http._

import scala.util.{ Try, Success, Failure }

import co.theasi.plotly.{Plot, Grid}

object PlotWriter {

  def draw(
      plot: Plot,
      fileName: String,
      fileOptions: FileOptions = FileOptions()
  )(implicit server: Server) = {
    if (fileOptions.overwrite) { deleteIfExists(fileName) }
    val drawnGrid = drawGrid(plot, fileName, fileOptions)
    val seriesAsJson = plot.series.zipWithIndex.map {
      case (series, index) =>
        val srcs = srcsFromDrawnGrid(drawnGrid, index)
        SeriesWriter.toJson(srcs, series.options)
    }
    val body = (
      ("figure" -> ("data" -> seriesAsJson)) ~
      ("filename" -> fileName) ~
      ("world_readable" -> true)
    )
    val request = Api.post("plots", compact(render(body)))
    val responseAsJson = Api.despatchAndInterpret(request)
    PlotFile.fromResponse(responseAsJson \ "file")
  }

  private def drawGrid(
      plot: Plot,
      fileName: String,
      fileOptions: FileOptions)
      (implicit server: Server)
  : GridFile = {
    val columns = plot.series.zipWithIndex.flatMap {
      case (s, index) =>
        List(s"x-$index" -> s.xs, s"y-$index" -> s.ys)
    }.toMap
    val grid = Grid(columns)
    GridWriter.draw(grid, fileName+"-grid", fileOptions)
  }

  private def srcsFromDrawnGrid(drawnGrid: GridFile, index: Int)
  : List[String] = {
    val xName = s"x-$index"
    val yName = s"y-$index"
    val xuid = drawnGrid.columnUids(xName)
    val yuid = drawnGrid.columnUids(yName)
    val xsrc = s"${drawnGrid.fileId}:$xuid"
    val ysrc = s"${drawnGrid.fileId}:$yuid"
    List(xsrc, ysrc)
  }

  private def deleteIfExists(fileName: String)(implicit server: Server) {
    Try { PlotFile.fromFileName(fileName) } match {
      case Success(plot) => // exists already -> delete
        Api.despatchAndInterpret(Api.delete(s"plots/${plot.fileId}"))
      case Failure(PlotlyException("Not found.")) => // good to go
      case Failure(e) => throw e // some other error -> re-throw
    }
  }

}
