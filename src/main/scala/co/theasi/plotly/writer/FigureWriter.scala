package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import scalaj.http._

import scala.util.{Try, Success, Failure}

import co.theasi.plotly._

object FigureWriter {

  def draw(
      figure: Figure,
      fileName: String,
      fileOptions: FileOptions = FileOptions()
  )(implicit server: Server) = {
    if (fileOptions.overwrite) { deleteIfExists(fileName) }
    val drawnGrid = drawGrid(figure, fileName, fileOptions)
    val body = plotAsJson(figure, drawnGrid, fileName)
    val request = Api.post("plots", compact(render(body)))
    val responseAsJson = Api.despatchAndInterpret(request)
    PlotFile.fromResponse(responseAsJson \ "file")
  }

  def plotAsJson(
      figure: Figure,
      drawnGrid: GridFile,
      fileName: String
  ): JObject = {

    val axisIndices = axisIndicesFromPlots(figure.plots)

    val allSeries = for {
      subplot <- figure.plots
      series <- subplot.series
    } yield series

    val seriesSrcs = for {
      (series, index) <- allSeries.zipWithIndex
      srcs = srcsFromDrawnGrid(drawnGrid, series, index)
    } yield srcs

    val seriesOptions = for {
      (series, index) <- allSeries.zipWithIndex
      newOptions = updateOptionsFromDrawnGrid(
        drawnGrid, series.options, index)
    } yield newOptions

    val seriesAxisIndex = for {
      (subplot, axisIndex) <- figure.plots.zip(axisIndices)
      series <- subplot.series
    } yield axisIndex

    val writeInfos = for {
      (srcs, options, axisIndex) <- (seriesSrcs, seriesOptions, seriesAxisIndex).zipped
      writeInfo = options match {
        case o: ScatterOptions => ScatterWriteInfo(srcs, axisIndex, o)
      }
    } yield writeInfo

    val seriesAsJson = writeInfos.map { SeriesWriter.toJson }

    val layoutFragments = for {
      (index, viewPort, plot) <- (axisIndices, figure.viewPorts, figure.plots).zipped
      fragment = plot match {
        case p: CartesianPlot =>
          CartesianPlotLayoutWriter.toJson(index, viewPort, p)
      }
    } yield fragment

    val fragmentsAsJson = layoutFragments.reduce { _ ~ _ }

    val optionsAsJson = FigureOptionsWriter.toJson(figure.options)

    val layout = fragmentsAsJson ~ optionsAsJson

    val body =
      ("figure" ->
        ("data" -> seriesAsJson) ~ ("layout" -> layout)
      ) ~
      ("filename" -> fileName) ~
      ("world_readable" -> true)
    println(compact(render(body)))
    body
  }

  private def drawGrid(
      figure: Figure,
      fileName: String,
      fileOptions: FileOptions)
      (implicit server: Server)
  : GridFile = {
    val allSeries = for {
      subplot <- figure.plots
      series <- subplot.series
    } yield series

    val columns = allSeries.zipWithIndex.flatMap {
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

  private def axisIndicesFromPlots(plots: Vector[Plot]) =
    plots.scanLeft(1) {
      (curIndex, plot) => plot match {
        case p: CartesianPlot => curIndex + 1
        case _ => curIndex
      }
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
    options: SeriesOptions,
    index: Int
  ): SeriesOptions = {
    options match {
      case o: ScatterOptions =>
        updateScatterOptionsFromDrawnGrid(drawnGrid, o, index)
      case o: BarOptions =>
        updateBarOptionsFromDrawnGrid(drawnGrid, o, index)
      case o: BoxOptions =>
        updateBoxOptionsFromDrawnGrid(drawnGrid, o, index)
    }
  }

  private def updateScatterOptionsFromDrawnGrid(
    drawnGrid: GridFile,
    options: ScatterOptions,
    index: Int
  ): ScatterOptions = {
    val newText = options.text.map {
      case IterableText(values) =>
        val textName = s"text-$index"
        val textUid = drawnGrid.columnUids(textName)
        val textSrc = s"${drawnGrid.fileId}:$textUid"
        SrcText(textSrc)
      case t => t
    }
    options.copy(text = newText)
  }

  private def updateBarOptionsFromDrawnGrid(
      drawnGrid: GridFile,
      options: BarOptions,
      index: Int
  ): BarOptions = options

  private def updateBoxOptionsFromDrawnGrid(
    drawnGrid: GridFile,
    options: BoxOptions,
    index: Int
  ): BoxOptions = options

  private def deleteIfExists(fileName: String)(implicit server: Server) {
    Try { PlotFile.fromFileName(fileName) } match {
      case Success(plot) => // exists already -> delete
        Api.despatchAndInterpret(Api.delete(s"plots/${plot.fileId}"))
      case Failure(PlotlyException("Not found.")) => // good to go
      case Failure(e) => throw e // some other error -> re-throw
    }
  }

}
