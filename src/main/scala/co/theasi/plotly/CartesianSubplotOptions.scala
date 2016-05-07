package co.theasi.plotly

case class CartesianSubplotOptions(xAxes: Vector[Axis], yAxes: Vector[Axis]) {
  def xAxisOptions(newOptions: AxisOptions): CartesianSubplotOptions = {
    val newAxis = xAxes(0).copy(options = newOptions)
    copy(xAxes = xAxes.updated(0, newAxis))
  }

  def yAxisOptions(newOptions: AxisOptions): CartesianSubplotOptions = {
    val newAxis = yAxes(0).copy(options = newOptions)
    copy(yAxes = yAxes.updated(0, newAxis))
  }
}

object CartesianSubplotOptions {
  def apply(): CartesianSubplotOptions =
    CartesianSubplotOptions(Vector(Axis()), Vector(Axis()))
}
