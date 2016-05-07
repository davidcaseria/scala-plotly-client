package co.theasi.plotly

trait Plot {
  def series: Vector[Series]
}

object Plot {
  def apply(): CartesianPlot = CartesianPlot()
}


case class CartesianPlot(series: Vector[Series]) extends Plot {

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
  ): Plot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = series :+ Bar(xsAsPType, ysAsPType, BarOptions()))
  }

  def withBox[X: Writable](
    xs: Iterable[X]
  ): Plot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    copy(series = series :+ Box(xsAsPType, BoxOptions()))
  }

}


object CartesianPlot {

  def apply(): CartesianPlot = CartesianPlot(Vector.empty[Series])

}

