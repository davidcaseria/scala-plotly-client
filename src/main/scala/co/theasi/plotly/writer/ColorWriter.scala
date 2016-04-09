package co.theasi.plotly.writer

import co.theasi.plotly.Color

object ColorWriter {
  def toJson(color: Color): String =
    s"rgba(${color.r}, ${color.g}, ${color.b}, ${color.a})"
}
