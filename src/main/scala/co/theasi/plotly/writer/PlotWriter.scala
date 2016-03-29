package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import scalaj.http._

import scala.util.{Try, Success, Failure}

import co.theasi.plotly._

object PlotWriter {

  def draw(
      plot: Plot,
      fileName: String,
      fileOptions: FileOptions = FileOptions()
  )(implicit server: Server) = {
    if (fileOptions.overwrite) { deleteIfExists(fileName) }
    val drawnGrid = drawGrid(plot, fileName, fileOptions)
    val body = plotAsJson(plot, drawnGrid, fileName)
    val request = Api.post("plots", compact(render(body)))
    val responseAsJson = Api.despatchAndInterpret(request)
    PlotFile.fromResponse(responseAsJson \ "file")
  }

  def plotAsJson(
      plot: Plot,
      drawnGrid: GridFile,
      fileName: String
  ): JObject = {
    val seriesAsJson = plot.series.zipWithIndex.map {
      case (series, index) =>
        val srcs = srcsFromDrawnGrid(drawnGrid, series, index)
        val newOptions = updateOptionsFromDrawnGrid(
          drawnGrid, series.options, index)
        SeriesWriter.toJson(srcs, newOptions)
    }
    val layoutAsJson = LayoutWriter.toJson(plot.layout)
    val body =
      ("figure" ->
        ("data" -> seriesAsJson) ~ ("layout" -> layoutAsJson)
      ) ~
      ("filename" -> fileName) ~
      ("world_readable" -> true)
    body
  }

  private def drawGrid(
      plot: Plot,
      fileName: String,
      fileOptions: FileOptions)
      (implicit server: Server)
  : GridFile = {
    val columns = plot.series.zipWithIndex.flatMap {
      case (s, index) => seriesToColumns(s, index)
    }.toMap
    val grid = Grid(columns)
    GridWriter.draw(grid, fileName+"-grid", fileOptions)
  }

  private def seriesToColumns(
      series: Series,
      index: Int
  ): List[(String, Iterable[PType])] = {

    val dataColumns = series match {
      case s: Series2D[_, _] =>
        List(s"x-$index" -> s.xs, s"y-$index" -> s.ys)
      case s: Series1D[_] =>
        List(s"x-$index" -> s.xs)
    }

    val optionColumns = series match {
      case s: Scatter[_, _] => scatterOptionsToColumns(s.options, index)
      case _ => List.empty[(String, Iterable[PType])]
    }

    dataColumns ++ optionColumns
  }

  def scatterOptionsToColumns(options: ScatterOptions, index: Int)
  : List[(String, Iterable[PType])] =
    options.text match {
      case Some(IterableText(values)) => List(s"text-$index" -> values)
      case _ => List.empty
    }

  private def srcsFromDrawnGrid(
      drawnGrid: GridFile,
      series: Series,
      index: Int
  ): List[String] = {
    val srcs = series match {
      case s: Series2D[_, _] =>
        val xName = s"x-$index"
        val yName = s"y-$index"
        val xuid = drawnGrid.columnUids(xName)
        val yuid = drawnGrid.columnUids(yName)
        val xsrc = s"${drawnGrid.fileId}:$xuid"
        val ysrc = s"${drawnGrid.fileId}:$yuid"
        List(xsrc, ysrc)
      case s: Series1D[_] =>
        val xName = s"x-$index"
        val xuid = drawnGrid.columnUids(xName)
        val xsrc = s"${drawnGrid.fileId}:$xuid"
        List(xsrc)
    }
    srcs
  }

  private def updateOptionsFromDrawnGrid(
      drawnGrid: GridFile,
      options: SeriesOptions[_],
      index: Int
  ): SeriesOptions[_] = {
    options match {
      case o: ScatterOptions =>
        val newText = o.text.map {
          case IterableText(values) =>
            val textName = s"text-$index"
            val textUid = drawnGrid.columnUids(textName)
            val textSrc = s"${drawnGrid.fileId}:$textUid"
            SrcText(textSrc)
          case t => t
        }
        o.copy(text = newText)
      case s => s
    }
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
