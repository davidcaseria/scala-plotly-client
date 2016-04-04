package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import co.theasi.plotly.Font

object FontWriter {
  def toJson(font: Font): Option[JObject] = None
}
