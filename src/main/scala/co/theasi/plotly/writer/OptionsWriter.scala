package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import co.theasi.plotly.{ScatterOptions, ScatterMode, MarkerOptions,
  Color, TextValue, StringText, IterableText, SrcText}

object OptionsWriter {

  def scatterOptionsToJson(options: ScatterOptions): JObject = {
    val xAxis = axisToJson(options.xAxis, "x")
    val yAxis = axisToJson(options.yAxis, "y")

    ("xaxis" -> xAxis) ~
    ("yaxis" -> yAxis) ~
    ("name" -> options.name) ~
    ("mode" -> scatterModeToJson(options.mode)) ~
    textToJson(options.text) ~
    ("marker" -> markerOptionsToJson(options.marker))
  }

  private def axisToJson(axis: Option[Int], root: String): Option[String] =
    axis.map {
      case 0 => root
      case i => root + (i+1).toString
    }

  private def scatterModeToJson(mode: Seq[ScatterMode.Value])
  : Option[String] =
    if (mode.isEmpty) { None }
    else { Some(mode.map { _.toString.toLowerCase }.mkString("+")) }

  private def markerOptionsToJson(options: MarkerOptions): JObject = {
    ("color" -> options.color.map(ColorWriter.toJson _)) ~
    ("size" -> options.size) ~
    ("symbol" -> options.symbol) ~
    ("line" ->
      (
        ("color" -> options.lineColor.map(ColorWriter.toJson _)) ~
        ("width" -> options.lineWidth)
      )
    )
  }

  private def textToJson(text: Option[TextValue]): JObject =
    text match {
      case Some(StringText(s)) => ("text" -> s)
      case Some(SrcText(s)) => ("textsrc" -> s)
      case Some(IterableText(v)) =>
        throw new IllegalStateException("No")
        // Should not happen at this stage
        // text series are replaced by textSrc in PlotWriter
      case None => JObject()
    }


}
