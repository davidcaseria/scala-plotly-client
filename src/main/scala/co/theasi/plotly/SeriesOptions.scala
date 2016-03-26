package co.theasi.plotly

trait SeriesOptions[T <: SeriesOptions[T]] {
  val name: Option[String]
  val xAxis: Option[Int]
  val yAxis: Option[Int]

  def name(newName: String): T
  def xAxis(newXAxis: Int): T
  def yAxis(newYAxis: Int): T
}

case class BoxOptions(
  name: Option[String] = None,
  xAxis: Option[Int] = None,
  yAxis: Option[Int] = None
) extends SeriesOptions[BoxOptions] {
  def name(newName: String) = copy(name = Some(newName))
  def xAxis(newXAxis: Int) = copy(xAxis = Some(newXAxis))
  def yAxis(newYAxis: Int): BoxOptions = copy(yAxis = Some(newYAxis))
}


case class ScatterOptions(
  name: Option[String],
  xAxis: Option[Int],
  yAxis: Option[Int],
  mode: Seq[ScatterMode.Value]
) extends SeriesOptions[ScatterOptions] {

  def name(newName: String) = copy(name = Some(newName))
  def xAxis(newXAxis: Int) = copy(xAxis = Some(newXAxis))
  def yAxis(newYAxis: Int): ScatterOptions = copy(yAxis = Some(newYAxis))

  def mode(newMode: ScatterMode.Value, rest: ScatterMode.Value*)
  : ScatterOptions = mode(newMode +: rest)
  def mode(newModes: Iterable[ScatterMode.Value]): ScatterOptions =
    copy(mode = newModes.toSeq)

}

object ScatterOptions {
  def apply(): ScatterOptions = ScatterOptions(
    name = None,
    xAxis = None,
    yAxis = None,
    mode = Seq.empty
  )
}

case class BarOptions(
  name: Option[String] = None,
  xAxis: Option[Int] = None,
  yAxis: Option[Int] = None
) extends SeriesOptions[BarOptions] {

  def name(newName: String) = copy(name = Some(newName))
  def xAxis(newXAxis: Int) = copy(xAxis = Some(newXAxis))
  def yAxis(newYAxis: Int): BarOptions = copy(yAxis = Some(newYAxis))

}

object ScatterMode extends Enumeration {
  val Marker = Value("markers")
  val Line = Value("lines")
  val Text = Value("text")
}
