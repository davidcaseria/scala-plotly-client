package co.theasi.plotly.writer

import co.theasi.plotly.{SeriesOptions, ScatterOptions,
  BarOptions, BoxOptions, SurfaceOptions}

trait SeriesWriteInfo {
  val srcs: List[String]
  val options: SeriesOptions
}

case class ScatterWriteInfo(
  srcs: List[String],
  axisIndex: Int,
  options: ScatterOptions
) extends SeriesWriteInfo

case class BarWriteInfo(
  srcs: List[String],
  axisIndex: Int,
  options: BarOptions
) extends SeriesWriteInfo

case class BoxWriteInfo(
  srcs: List[String],
  axisIndex: Int,
  options: BoxOptions
) extends SeriesWriteInfo

case class SurfaceWriteInfo(
  srcs: List[String],
  options: SurfaceOptions
) extends SeriesWriteInfo
