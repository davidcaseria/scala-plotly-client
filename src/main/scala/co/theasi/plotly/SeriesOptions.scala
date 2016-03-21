package co.theasi.plotly

sealed trait SeriesOptions {}

case class BoxOptions(
  name: String = ""
)
extends SeriesOptions

case class ScatterOptions(
  name: String = ""
)
extends SeriesOptions

case class BarOptions(
  name: String = ""
)
extends SeriesOptions
