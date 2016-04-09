package co.theasi.plotly

case class LayoutOptions(
  title: Option[String],
  legendOptions: LegendOptions,
  margins: Margins
)

object LayoutOptions {
  def apply(): LayoutOptions = LayoutOptions(
    title = None,
    legendOptions = LegendOptions(),
    margins = Margins()
  )
}
