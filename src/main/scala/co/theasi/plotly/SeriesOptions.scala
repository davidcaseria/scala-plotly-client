package co.theasi.plotly

trait SeriesOptions[T <: SeriesOptions[T]] {
  val name: Option[String]
  val xAxis: Option[Int]
  val yAxis: Option[Int]

  def name(newName: String): T
  def xAxis(newXAxis: Int): T
  def yAxis(newYAxis: Int): T

  def onSubplot(subplotRef: Int): T
}

case class BoxOptions(
  name: Option[String] = None,
  xAxis: Option[Int] = None,
  yAxis: Option[Int] = None
) extends SeriesOptions[BoxOptions] {
  def name(newName: String) = copy(name = Some(newName))
  def xAxis(newXAxis: Int) = copy(xAxis = Some(newXAxis))
  def yAxis(newYAxis: Int): BoxOptions = copy(yAxis = Some(newYAxis))
  def onSubplot(subplotRef: Int): BoxOptions =
    copy(xAxis = Some(subplotRef), yAxis = Some(subplotRef))
}


case class BarOptions(
  name: Option[String] = None,
  xAxis: Option[Int] = None,
  yAxis: Option[Int] = None
) extends SeriesOptions[BarOptions] {

  def name(newName: String) = copy(name = Some(newName))
  def xAxis(newXAxis: Int) = copy(xAxis = Some(newXAxis))
  def yAxis(newYAxis: Int): BarOptions = copy(yAxis = Some(newYAxis))
  def onSubplot(subplotRef: Int): BarOptions =
    copy(xAxis = Some(subplotRef), yAxis = Some(subplotRef))

}
