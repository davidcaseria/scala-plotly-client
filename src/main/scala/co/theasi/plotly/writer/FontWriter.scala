package co.theasi.plotly.writer

import org.json4s._
import org.json4s.JsonDSL._

import co.theasi.plotly.{Font, emptyFont}

object FontWriter {
  def toJson(font: Font): Option[JObject] = font match {
    case emptyFont() => None
    case _ => Some(
      ("color" -> font.color.map { ColorWriter.toJson }) ~
      ("family" -> font.family.map { _.mkString("\"", "\", \"", "\"")}) ~
      ("size" -> font.size)
    )
  }
}
