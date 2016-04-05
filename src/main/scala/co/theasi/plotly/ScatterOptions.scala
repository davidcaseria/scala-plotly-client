package co.theasi.plotly

case class ScatterOptions(
  name: Option[String],
  xAxis: Option[Int],
  yAxis: Option[Int],
  mode: Seq[ScatterMode.Value],
  text: Option[TextValue],
  marker: MarkerOptions
) extends SeriesOptions[ScatterOptions] {

  def name(newName: String) = copy(name = Some(newName))
  def xAxis(newXAxis: Int) = copy(xAxis = Some(newXAxis))
  def yAxis(newYAxis: Int): ScatterOptions = copy(yAxis = Some(newYAxis))
  def onAxes(axesRef: (Int, Int)): ScatterOptions = {
    val (xAxisRef, yAxisRef) = axesRef
    copy(xAxis = Some(xAxisRef), yAxis = Some(yAxisRef))
  }

  def mode(newMode: ScatterMode.Value, rest: ScatterMode.Value*)
  : ScatterOptions = mode(newMode +: rest)
  def mode(newModes: Iterable[ScatterMode.Value]): ScatterOptions =
    copy(mode = newModes.toSeq)

  def text(newText: String): ScatterOptions =
    copy(text = Some(StringText(newText)))
  def text[T: Writable](newText: Iterable[T]): ScatterOptions = {
    val textAsPType = newText.map { implicitly[Writable[T]].toPType }
    copy(text = Some(IterableText(textAsPType)))
  }
  def textSrc(src: String): ScatterOptions =
    copy(text = Some(SrcText(src)))

  def marker(newMarker: MarkerOptions): ScatterOptions =
    copy(marker = newMarker)

  def updatedMarker(updater: MarkerOptions => MarkerOptions)
  : ScatterOptions = {
    val newMarker = updater(marker)
    marker(newMarker)
  }

}

object ScatterOptions {
  def apply(): ScatterOptions = ScatterOptions(
    name = None,
    xAxis = None,
    yAxis = None,
    mode = Seq.empty,
    text = None,
    marker = MarkerOptions()
  )
}

object ScatterMode extends Enumeration {
  val Marker = Value("markers")
  val Line = Value("lines")
  val Text = Value("text")
}

case class MarkerOptions(
  size: Option[Int],
  color: Option[Color],
  symbol: Option[String],
  lineWidth: Option[Int],
  lineColor: Option[Color]
) {

  def size(newSize: Int): MarkerOptions = copy(size = Some(newSize))
  def color(newColor: Color): MarkerOptions =
    copy(color = Some(newColor))
  def color(r: Int, g: Int, b: Int, a: Double): MarkerOptions =
    color(Color.rgba(r, g, b, a))
  def color(r: Int, g: Int, b: Int): MarkerOptions =
    color(r, g, b, 1.0)

  def symbol(newSymbol: String): MarkerOptions =
    copy(symbol = Some(newSymbol))

  def lineWidth(newLineWidth: Int): MarkerOptions =
    copy(lineWidth = Some(newLineWidth))
  def lineColor(newLineColor: Color): MarkerOptions =
    copy(lineColor = Some(newLineColor))
  def lineColor(r: Int, g: Int, b: Int, a: Double): MarkerOptions =
    lineColor(Color.rgba(r, g, b, a))
  def lineColor(r: Int, g: Int, b: Int): MarkerOptions =
    lineColor(r, g, b, 1.0)
}

object MarkerOptions {
  def apply(): MarkerOptions = MarkerOptions(
    size = None,
    color = None,
    symbol = None,
    lineWidth = None,
    lineColor = None
  )
}
