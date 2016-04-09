package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import co.theasi.plotly.{Layout, Axis, AxisOptions, LayoutOptions, LegendOptions, emptyLegendOptions, Margins, emptyMargins}

object LayoutWriter {
  def toJson(layout: Layout[_]): JObject = {
    val xAxesAsJson = axesAsJson(layout.xAxes, "xaxis", "y")
    val yAxesAsJson = axesAsJson(layout.yAxes, "yaxis", "x")
    xAxesAsJson ~
    yAxesAsJson ~
    layoutOptionsAsJson(layout.options)
  }

  private def layoutOptionsAsJson(options: LayoutOptions): JObject = (
    ("title" -> options.title) ~
    ("legend" -> legendAsJson(options.legendOptions)) ~
    ("margin" -> marginsAsJson(options.margins))
  )

  private def axesAsJson(
      axes: Vector[Axis],
      radix: String,
      anchorRadix: String
  ): JObject =
    axes.zipWithIndex.map { case (axis, index) =>
      val label = radix + (if(index == 0) "" else (index+1).toString)
      val obj = axisAsJson(axis, anchorRadix)
      label -> obj
    }.toList

  private def axisAsJson(axis: Axis, anchorRadix: String): JObject = {
    val (start, end) = axis.domain
    val anchorString = anchorRadix + (
      if(axis.anchor == 0) "" else (axis.anchor+1).toString)
    (
      ("domain" -> List(start, end)) ~
      ("anchor" -> anchorString) ~
      axisOptionsAsJson(axis.options)
    )
  }

  private def axisOptionsAsJson(options: AxisOptions): JObject = (
    ("title" -> options.title) ~
    ("ticklen" -> options.tickLength) ~
    ("zeroline" -> options.zeroLine) ~
    ("gridwidth" -> options.gridWidth) ~
    ("showgrid" -> options.grid) ~
    ("showline" -> options.line) ~
    ("linecolor" -> options.lineColor.map(ColorWriter.toJson _)) ~
    ("titlefont" -> FontWriter.toJson(options.titleFont)) ~
    ("tickfont" -> FontWriter.toJson(options.tickFont)) ~
    ("autotick" -> options.autoTick) ~
    ("dtick" -> options.tickSpacing) ~
    ("tickcolor" -> options.tickColor.map(ColorWriter.toJson _))
  )

  private def legendAsJson(options: LegendOptions): Option[JObject] =
    options match {
      case emptyLegendOptions() => None
      case _ => Some(
        ("x" -> options.x) ~
        ("y" -> options.y) ~
        ("xanchor" -> options.xAnchor.map { _.toString }) ~
        ("yanchor" -> options.yAnchor.map { _.toString }) ~
        ("bordercolor" -> options.borderColor.map { ColorWriter.toJson }) ~
        ("bgcolor" -> options.backgroundColor.map { ColorWriter.toJson }) ~
        ("font" -> FontWriter.toJson(options.font)) ~
        ("borderwidth" -> options.borderWidth)
      )
    }

  private def marginsAsJson(margins: Margins): Option[JObject] =
    margins match {
      case emptyMargins() => None
      case _ => Some(
        ("l" -> margins.left) ~
        ("r" -> margins.right) ~
        ("b" -> margins.bottom) ~
        ("t" -> margins.top)
      )
    }
}
