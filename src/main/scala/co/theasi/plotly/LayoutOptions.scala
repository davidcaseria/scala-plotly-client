package co.theasi.plotly

case class LayoutOptions(
  title: Option[String],
  legendOptions: LegendOptions
)

object LayoutOptions {
  def apply(): LayoutOptions = LayoutOptions(
    title = None,
    legendOptions = LegendOptions()
  )
}
