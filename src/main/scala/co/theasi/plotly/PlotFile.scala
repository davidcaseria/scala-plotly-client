package co.theasi.plotly

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

case class PlotFile(
  fileId: String,
  fileName: String = "")


object PlotFile {

  def fromFileName(fileName: String)(implicit server: Server)
  : PlotFile = {
    val request = Api.get("plots/lookup", Seq("path" -> fileName))
    val response = Api.despatchAndInterpret(request)
    fromResponse(response)
  }

  def fromResponse(response: JValue): PlotFile = {
    val JString(fid) = (response \ "fid")
    val JString(fileName) = (response \ "filename")
    new PlotFile(fid, fileName)
  }

}
