package co.theasi.plotly

sealed trait SeriesOptions {
  val name: String
  val xAxis: Int
  val yAxis: Int
}

case class BoxOptions(
  name: String = "",
  xAxis: Int = 0,
  yAxis: Int = 0
)
extends SeriesOptions

case class ScatterOptions(
  name: String = "",
  xAxis: Int = 0,
  yAxis: Int = 0
)
extends SeriesOptions

case class BarOptions(
  name: String = "",
  xAxis: Int = 0,
  yAxis: Int = 0
)
extends SeriesOptions
