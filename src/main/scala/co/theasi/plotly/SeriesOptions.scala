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


case class BarOptions(
  name: Option[String] = None,
  xAxis: Option[Int] = None,
  yAxis: Option[Int] = None
) extends SeriesOptions[BarOptions] {

  def name(newName: String) = copy(name = Some(newName))
  def xAxis(newXAxis: Int) = copy(xAxis = Some(newXAxis))
  def yAxis(newYAxis: Int): BarOptions = copy(yAxis = Some(newYAxis))

}
