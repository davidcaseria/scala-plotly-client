package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import co.theasi.plotly.{ScatterOptions, ScatterMode, MarkerOptions, Color}

object OptionsWriter {

  def scatterOptionsToJson(options: ScatterOptions): JObject = {
    val xAxis = axisToJson(options.xAxis, "x")
    val yAxis = axisToJson(options.yAxis, "y")

    ("xaxis" -> xAxis) ~
    ("yaxis" -> yAxis) ~
    ("name" -> options.name) ~
    ("mode" -> scatterModeToJson(options.mode)) ~
    ("marker" -> markerOptionsToJson(options.marker))
  }

  private def axisToJson(axis: Option[Int], root: String): Option[String] =
    axis.map {
      case 0 => root
      case i => root + (i+1).toString
    }

  private def scatterModeToJson(mode: Seq[ScatterMode.Value]): String =
    if (mode.isEmpty) { "none" } else { mode.map { _.toString.toLowerCase }.mkString("+") }

  private def markerOptionsToJson(options: MarkerOptions): JObject = {
    ("color" -> options.color.map(colorToJson)) ~
    ("size" -> options.size) ~
    ("line" ->
      (
        ("color" -> options.lineColor.map(colorToJson)) ~
        ("width" -> options.lineWidth)
      )
    )
  }

  private def colorToJson(color: Color): String =
    s"rgba(${color.r}, ${color.g}, ${color.b}, ${color.a})"

}
