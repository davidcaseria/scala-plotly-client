package co.theasi.plotly

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

case class DrawnPlot(
  fileId: String,
  fileName: String = "")


object DrawnPlot {

  def fromFileName(fileName: String)(implicit api: Api): DrawnPlot = {
    val request = api.get("plots/lookup", Seq("path" -> fileName))
    val response = api.despatchAndInterpret(request)
    fromResponse(response)
  }

  def fromResponse(response: JValue): DrawnPlot = {
    val JString(fid) = (response \ "fid")
    val JString(fileName) = (response \ "filename")
    new DrawnPlot(fid, fileName)
  }

}
