package co.theasi.plotly

case class SurfaceOptions(
  name: Option[String],
  opacity: Option[Double],
  showScale: Option[Boolean]
) extends SeriesOptions {
  def name(newName: String): SurfaceOptions =
    copy(name = Some(newName))

  def opacity(newOpacity: Double): SurfaceOptions = {
    require(
      newOpacity >= 0.0 && newOpacity <= 1.0,
      "Opacity must be between 0 and 1")
    copy(opacity = Some(newOpacity))
  }

  def withScale(): SurfaceOptions = copy(showScale = Some(true))
  def noScale(): SurfaceOptions = copy(showScale = Some(false))
}


object SurfaceOptions {
  def apply(): SurfaceOptions = SurfaceOptions(
    name = None,
    opacity = None,
    showScale = None
  )
}
