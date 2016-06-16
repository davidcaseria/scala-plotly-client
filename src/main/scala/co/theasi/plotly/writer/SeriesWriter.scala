package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import co.theasi.plotly.{SeriesOptions, ScatterOptions, BarOptions, BoxOptions}

object SeriesWriter {
  def toJson(seriesWriteInfo: SeriesWriteInfo)
  : JValue = {
    seriesWriteInfo match {
      case s: ScatterWriteInfo => scatterToJson(s)
      case s: BarWriteInfo => barToJson(s)
      case s: BoxWriteInfo => boxToJson(s)
      case s: SurfaceWriteInfo => surfaceToJson(s)
    }
  }

  private def scatterToJson(info: ScatterWriteInfo)
  : JValue = {
    val List(xsrc, ysrc) = info.srcs

    ("xsrc" -> xsrc) ~
    ("ysrc" -> ysrc) ~
    axisToJson(info.axisIndex) ~
    OptionsWriter.scatterOptionsToJson(info.options)
  }

  private def barToJson(info: BarWriteInfo)
  : JValue = {
    val List(xsrc, ysrc) = info.srcs
    ("xsrc" -> xsrc) ~
    ("ysrc" -> ysrc) ~
    axisToJson(info.axisIndex) ~
    ("type" -> "bar") ~
    OptionsWriter.barOptionsToJson(info.options)
  }

  private def boxToJson(info: BoxWriteInfo)
  : JValue = {
    val List(xsrc) = info.srcs
    ("ysrc" -> xsrc) ~ axisToJson(info.axisIndex) ~ ("type" -> "box")
  }

  private def surfaceToJson(info: SurfaceWriteInfo)
  : JValue = {
    val List(zsrc) = info.srcs
    ("zsrc" -> zsrc) ~
    ("type" -> "surface") ~
    sceneToJson(info.sceneIndex) ~
    OptionsWriter.surfaceOptionsToJson(info.options)
  }

  private def axisToJson(axisIndex: Int): JObject =
    axisIndex match {
      case 1 => ("xaxis" -> "x") ~ ("yaxis" -> "y")
      case i => ("xaxis" -> s"x$i") ~ ("yaxis" -> s"y$i")
    }

  private def sceneToJson(plotIndex: Int): JObject =
    "scene" -> ("scene" + stringifyPlotIndex(plotIndex))

  private def stringifyPlotIndex(plotIndex: Int): String =
    plotIndex match {
      case 1 => ""
      case i => i.toString
    }
}
