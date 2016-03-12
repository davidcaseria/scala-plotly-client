package co.theasi.plotly

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

case class DrawnGrid(
    fileId: String,
    fileName: String = "",
    columnUids: Map[String, String] = Map.empty)


object DrawnGrid {
  def fromFileName(fileName: String)(implicit api: Api): DrawnGrid = {
    val request = api.get("grids/lookup", Seq("path" -> fileName))
    val response = api.despatchAndInterpret(request)
    fromResponse(response)
  }

  def fromResponse(response: JValue): DrawnGrid = {
    val JString(fid) = (response \ "fid")
    val JString(fileName) = (response \ "filename")
    val JArray(columns) = (response \ "cols")
    val columUids = columns.map { col =>
      val JString(name) = (col \ "name")
      val JString(uid) = (col \ "uid")
      name -> uid
    }.toMap
    new DrawnGrid(fid, fileName, columUids)
  }
}
