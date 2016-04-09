package co.theasi.plotly

case class Margins(
  top: Option[Int],
  right: Option[Int],
  bottom: Option[Int],
  left: Option[Int]
)

object Margins {
  def apply(): Margins = Margins(None, None, None, None)
}

object emptyMargins {
  def unapply(margin: Margins): Boolean = margin == Margins()
}
