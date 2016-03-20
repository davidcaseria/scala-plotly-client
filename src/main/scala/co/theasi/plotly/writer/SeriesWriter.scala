package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import co.theasi.plotly.{SeriesOptions, ScatterOptions, BarOptions}

object SeriesWriter {
  def toJson(srcs: List[String], options: SeriesOptions)
  : JValue = {
    options match {
      case o: ScatterOptions => scatterToJson(srcs, o)
      case o: BarOptions => barToJson(srcs, o)
    }
  }

  private def scatterToJson(srcs: List[String], options: ScatterOptions)
  : JValue = {
    val List(xsrc, ysrc) = srcs
    ("xsrc" -> xsrc) ~ ("ysrc" -> ysrc)
  }

  private def barToJson(srcs: List[String], options: BarOptions)
  : JValue = {
    val List(xsrc, ysrc) = srcs
    ("xsrc" -> xsrc) ~ ("ysrc" -> ysrc) ~ ("type" -> "bar")
  }
}
