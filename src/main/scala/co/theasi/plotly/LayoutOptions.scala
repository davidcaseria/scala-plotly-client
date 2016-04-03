package co.theasi.plotly

case class LayoutOptions(
  title: Option[String]
)

object LayoutOptions {
  def apply(): LayoutOptions = LayoutOptions(
    title = None
  )
}
