package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import co.theasi.plotly.{Layout, Axis}

object LayoutWriter {
  def toJson(layout: Layout): JObject = {
    val xAxesAsJson = axesAsJson(layout.xAxes)
    val yAxesAsJson = axesAsJson(layout.yAxes)
    xAxesAsJson ~ yAxesAsJson
  }

  private def axesAsJson(axes: List[Axis]): JObject =
    axes.zipWithIndex.map { case (axis, index) =>
      val label = "xaxis" + (if(index == 0) "" else (index+1).toString)
      val obj = axisAsJson(axis)
      label -> obj
    }

  private def axisAsJson(axis: Axis): JObject = {
    val (start, end) = axis.domain
    ("domain" -> List(start, end))
  }
}
