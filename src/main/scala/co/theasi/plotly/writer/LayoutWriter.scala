package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import co.theasi.plotly.{Layout, Axis, AxisOptions, LayoutOptions}

object LayoutWriter {
  def toJson(layout: Layout[_]): JObject = {
    val xAxesAsJson = axesAsJson(layout.xAxes, "xaxis", "y")
    val yAxesAsJson = axesAsJson(layout.yAxes, "yaxis", "x")
    xAxesAsJson ~ yAxesAsJson ~ layoutOptionsAsJson(layout.options)
  }

  private def layoutOptionsAsJson(options: LayoutOptions): JObject = (
    "title" -> options.title
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
    ("gridwidth" -> options.gridWidth) ~
    ("zeroline" -> options.zeroLine)
  )
}
