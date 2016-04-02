package co.theasi.plotly

case class Axis(
  domain: (Double, Double),
  anchor: Int,
  options: AxisOptions
)

object Axis {
  def apply(domain: (Double, Double), anchor: Int): Axis =
    Axis(domain, anchor, AxisOptions())
}
