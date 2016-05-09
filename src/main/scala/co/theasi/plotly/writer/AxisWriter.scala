package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import co.theasi.plotly.ViewPort

object AxisWriter {
  
  def toJson(
      axisIndices: Vector[Int],
      viewPorts: Vector[ViewPort])
  : JObject = {
    val xDomains = viewPorts.map { _.xDomain }
    val yDomains = viewPorts.map { _.yDomain }
    axesToJson(axisIndices, xDomains, "xaxis", "y") ~
    axesToJson(axisIndices, yDomains, "yaxis", "x")
  }

  private def axesToJson(
      axisIndices: Vector[Int],
      domains: Vector[(Double, Double)],
      radix: String,
      anchorRadix: String
  ): JObject = {
    val axisList = domains.zip(axisIndices).map { case (domain, index) =>
      val indexString = (if(index == 1) "" else index.toString)
      val label = radix + indexString
      val (start, end) = domain
      val body = (
        ("domain" -> List(start, end)) ~
        ("anchor" -> (anchorRadix + indexString))
      )
      JField(label, body)
    }.toList
    JObject(axisList)
  }


}
