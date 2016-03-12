package co.theasi.plotly

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import scala.util.{ Try, Success, Failure }

case class Grid(
    fileName: String,
    columns: Map[String, Iterable[PType]] = Map.empty,
    fileOptions: FileOptions = FileOptions()
) {

  def draw(implicit api: Api): DrawnGrid = {
    if(fileOptions.overwrite) {
      Try { DrawnGrid.fromFileName(fileName)(api) } match {
        case Success(grid) => // exists already -> delete
          api.despatchAndInterpret(api.delete(s"grids/${grid.fileId}"))
        case Failure(PlotlyException("Not found.")) => // good to go
        case Failure(e) => throw e // some other error -> re-throw
      }
    }
    val request = api.post("grids", compact(render(gridAsJson)))
    val parsedResponse = api.despatchAndInterpret(request)
    DrawnGrid.fromResponse(parsedResponse \ "file")
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
