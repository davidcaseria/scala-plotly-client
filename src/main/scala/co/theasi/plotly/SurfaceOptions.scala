package co.theasi.plotly

case class SurfaceOptions(name: Option[String]) extends SeriesOptions


object SurfaceOptions {
  def apply(): SurfaceOptions = SurfaceOptions(
    name = None
  )
}
