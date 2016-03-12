package co.theasi.plotly

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import scalaj.http._

case class Plot(
  val fileName: String,
  val series: List[Series2D[PType, PType]] = List.empty
) {

  def withScatter[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y]
  ): Plot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = Scatter(xsAsPType, ysAsPType) :: series)
  }

  def grid: Grid = {
    val columns = series.zipWithIndex.flatMap {
      case (s, index) =>
        List(s"x-$index" -> s.xs, s"y-$index" -> s.ys)
    }.toMap
    Grid(fileName+"-grid", columns)
  }

  def draw(implicit api: Api) {
    val drawnGrid = grid.draw(api)
    val seriesAsJson = series.zipWithIndex.map { case (series, index) =>
      val xName = s"x-$index"
      val yName = s"y-$index"
      val xuid = drawnGrid.columnUids(xName)
      val yuid = drawnGrid.columnUids(yName)
      val xsrc = s"${drawnGrid.fileId}:$xuid"
      val ysrc = s"${drawnGrid.fileId}:$yuid"
      ("xsrc" -> xsrc) ~ ("ysrc" -> ysrc)
    }
    val body = (
      ("figure" -> ("data" -> seriesAsJson)) ~
      ("filename" -> fileName) ~
      ("world_readable" -> true)
    )
    println(compact(render(body)))
    val request = api.post("plots", compact(render(body)))
    println(request)
    val response = request.asString
    println(response)
    val responseAsJson = parse(response.body)
    val JString(newFileName) = (responseAsJson \ "file" \ "filename")
    val JString(fileId) = (responseAsJson \ "file" \ "fid")
    DrawnPlot(fileId, newFileName)
  }
}
