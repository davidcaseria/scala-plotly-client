package co.theasi.plotly.writer

import org.json4s._
import org.json4s.JsonDSL._

import co.theasi.plotly.{ViewPort, ThreeDPlot}

object ThreeDPlotLayoutWriter {

  def toJson(
    sceneIndex: Int,
    viewPort: ViewPort,
    plot: ThreeDPlot)
  : JObject = {
    val sceneDomain = sceneDomainToJson(sceneIndex, viewPort)
    sceneDomain
  }

  private def sceneDomainToJson(sceneIndex: Int, viewPort: ViewPort): JObject = {
    val indexString = (if(sceneIndex == 1) "" else sceneIndex.toString)
    val label = "scene" + indexString
    val body = ("domain" ->
      ("x" -> pairToList(viewPort.xDomain)) ~
      ("y" -> pairToList(viewPort.yDomain))
    )
    JObject(JField(label, body))
  }

  private def pairToList(pair: (Double, Double)): List[Double] = {
    val (start, end) = pair
    List(start, end)
  }

}
