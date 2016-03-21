package co.theasi.plotly

case class Plot(
    val series: List[Series] = List.empty
) {

  def withScatter[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y]
  ): Plot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = Scatter(xsAsPType, ysAsPType, ScatterOptions()) :: series)
  }

  def withBar[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y]
  ): Plot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = Bar(xsAsPType, ysAsPType, BarOptions()) :: series)
  }

  def withBox[X: Writable](
    xs: Iterable[X]
  ): Plot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    copy(series = Box(xsAsPType, BoxOptions()) :: series)
  }

}
