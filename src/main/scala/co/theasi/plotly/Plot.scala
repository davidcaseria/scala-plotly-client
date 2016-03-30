package co.theasi.plotly

case class Plot(
    val series: Vector[Series] = Vector.empty,
    val layout: Layout = Layout()
) {

  def layout(newLayout: Layout): Plot = copy(layout = newLayout)

  def withScatter[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y],
      options: ScatterOptions = ScatterOptions()
  ): Plot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = series :+ Scatter(xsAsPType, ysAsPType, options))
  }

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
