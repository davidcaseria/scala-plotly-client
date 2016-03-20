package co.theasi.plotly

case class Plot(
    val series: List[Series2D[PType, PType]] = List.empty
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

}
