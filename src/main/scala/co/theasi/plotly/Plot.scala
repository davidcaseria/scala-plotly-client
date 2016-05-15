package co.theasi.plotly

trait Plot {
  type OptionType
  def series: Vector[Series]
  val options: OptionType
}

object Plot {
  def apply(): CartesianPlot = CartesianPlot()
}


case class CartesianPlot(
    series: Vector[CartesianSeries],
    options: CartesianPlotOptions)
extends Plot {
  type OptionType = CartesianPlotOptions

  def withScatter[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y],
      options: ScatterOptions = ScatterOptions()
  ): CartesianPlot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = series :+ Scatter(xsAsPType, ysAsPType, options))
  }

  /** Add a bar series to this plot. */
  def withBar[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y]
  ): CartesianPlot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = series :+ Bar(xsAsPType, ysAsPType, BarOptions()))
  }

  def withBox[X: Writable](
    xs: Iterable[X]
  ): CartesianPlot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    copy(series = series :+ Box(xsAsPType, BoxOptions()))
  }

  def xAxisOptions(newAxisOptions: AxisOptions): CartesianPlot = {
    val newAxis = options.xAxis.copy(options = newAxisOptions)
    val newOptions = options.copy(xAxis = newAxis)
    copy(options = newOptions)
  }

  def yAxisOptions(newAxisOptions: AxisOptions): CartesianPlot = {
    val newAxis = options.yAxis.copy(options = newAxisOptions)
    val newOptions = options.copy(yAxis = newAxis)
    copy(options = newOptions)
  }

}


object CartesianPlot {

  def apply(): CartesianPlot = CartesianPlot(
    Vector.empty[CartesianSeries], CartesianPlotOptions())

}


case class ThreeDPlot(
  series: Vector[Series],
  options: ThreeDPlotOptions)
extends Plot {

  type OptionType = ThreeDPlotOptions

  def withSurface[Z: Writable](zs: Iterable[Iterable[Z]]): ThreeDPlot = {
    val zsAsPType = zs.map { zRow =>
      zRow.map { implicitly[Writable[Z]].toPType }
    }
    copy(series = series :+ SurfaceZ(zsAsPType, SurfaceOptions()))

  }

}


object ThreeDPlot {
  def apply(): ThreeDPlot = ThreeDPlot(
    Vector.empty[Series], ThreeDPlotOptions())
}
