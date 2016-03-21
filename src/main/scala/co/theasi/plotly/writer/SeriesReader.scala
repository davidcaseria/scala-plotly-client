package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._

import co.theasi.plotly._

object SeriesReader {
  def fromJson(json: JObject): Series2D[PType, PType] = {
    val seriesType = json \ "type"
    seriesType match {
      case JString("bar") => barFromJson(json)
      case JNothing => scatterFromJson(json)
      case _ => throw new UnexpectedServerResponse(
        s"Unrecognized series type: $seriesType")
    }
  }

  private def barFromJson(json: JObject): Bar[PType, PType] = {
    val (xs, ys) = xyFromJson(json)
    Bar(xs, ys, BarOptions())
  }

  private def scatterFromJson(json: JObject): Scatter[PType, PType] = {
    val (xs, ys) = xyFromJson(json)
    Scatter(xs, ys, ScatterOptions())
  }

  private def xyFromJson(json: JObject): (List[PType], List[PType]) = {
    val JArray(xDataAsJson) = json \ "x"
    val xs = columnFromJson(xDataAsJson)
    val JArray(yDataAsJson) = json \ "y"
    val ys = columnFromJson(yDataAsJson)
    (xs, ys)
  }

  private def columnFromJson(data: List[JValue]): List[PType] =
    data.map { jsonToPType _ }

  private def jsonToPType[X <: JValue](x: X) = x match {
    case JInt(i) => PInt(i.toInt)
    case JDouble(d) => PDouble(d)
    case JString(s) => PString(s)
  }
}
