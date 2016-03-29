package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import co.theasi.plotly.{SeriesOptions, ScatterOptions, BarOptions, BoxOptions}

object SeriesWriter {
  def toJson(srcs: List[String], options: SeriesOptions[_])
  : JValue = {
    options match {
      case o: ScatterOptions => scatterToJson(srcs, o)
      case o: BarOptions => barToJson(srcs, o)
      case o: BoxOptions => boxToJson(srcs, o)
    }
  }

  private def scatterToJson(srcs: List[String], options: ScatterOptions)
  : JValue = {
    val List(xsrc, ysrc) = srcs
    val optionsAsJson = OptionsWriter.scatterOptionsToJson(options)
    ("xsrc" -> xsrc) ~ ("ysrc" -> ysrc) ~ optionsAsJson
  }

  private def barToJson(srcs: List[String], options: BarOptions)
  : JValue = {
    val List(xsrc, ysrc) = srcs
    ("xsrc" -> xsrc) ~ ("ysrc" -> ysrc) ~ ("type" -> "bar")
  }

  private def boxToJson(srcs: List[String], options: BoxOptions)
  : JValue = {
    val List(xsrc) = srcs
    ("ysrc" -> xsrc) ~ ("type" -> "box")
  }
}
