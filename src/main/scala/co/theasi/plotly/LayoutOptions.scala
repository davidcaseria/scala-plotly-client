package co.theasi.plotly

case class LayoutOptions(
  title: Option[String],
  legendOptions: LegendOptions,
  margins: Margins,
  width: Option[Int],
  height: Option[Int],
  paperBackgroundColor: Option[Color],
  plotBackgroundColor: Option[Color]
)

object LayoutOptions {
  def apply(): LayoutOptions = LayoutOptions(
    title = None,
    legendOptions = LegendOptions(),
    margins = Margins(),
    width = None,
    height = None,
    paperBackgroundColor = None,
    plotBackgroundColor = None
  )
}
