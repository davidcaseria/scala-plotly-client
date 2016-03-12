package co.theasi.plotly

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

case class Grid(
    fileName: String,
    columns: Map[String, Iterable[PType]] = Map.empty
) {

  def draw(implicit api: Api): DrawnGrid = {
    val request = api.post("grids", compact(render(gridAsJson)))
    val response = request.asString
    val parsedResponse = parse(response.body)
    val JString(fid) = (parsedResponse \ "file" \ "fid")
    val JString(fileName) = (parsedResponse \ "file" \ "filename")
    val JArray(columns) = (parsedResponse \ "file" \ "cols")
    val columUids = columns.map { col =>
      val JString(name) = (col \ "name")
      val JString(uid) = (col \ "uid")
      name -> uid
    }.toMap
    DrawnGrid(fid, fileName, columUids)
  }

  private def gridAsJson: JObject = {
    val columnsAsJson = columns.toIterator.zipWithIndex.map {
      case ((columnName, columnValues), index) =>
        ColumnWriter.toJson(columnValues, columnName, index)
    }
    val jsonObj = columnsAsJson.reduce {
      (memo, current) => memo ~ current
    }
    (
      ("data" -> ("cols" -> jsonObj)) ~
      ("filename" -> fileName)
    )
  }


}
