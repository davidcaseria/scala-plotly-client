package co.theasi.plotly

case class AxisOptions(
  title: Option[String],
  tickLength: Option[Int],
  zeroLine: Option[Boolean],
  gridWidth: Option[Int]
) {
  def title(newTitle: String): AxisOptions = copy(title = Some(newTitle))

  def tickLength(newTickLength: Int): AxisOptions =
    copy(tickLength = Some(newTickLength))

  def withZeroLine: AxisOptions = copy(zeroLine = Some(true))
  def noZeroLine: AxisOptions = copy(zeroLine = Some(false))

  def gridWidth(newGridWidth: Int): AxisOptions =
    copy(gridWidth = Some(newGridWidth))
}

object AxisOptions {
  def apply(): AxisOptions = AxisOptions(
    title = None,
    tickLength = None,
    zeroLine = None,
    gridWidth = None
  )
}
