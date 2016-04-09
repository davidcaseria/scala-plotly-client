package co.theasi.plotly

case class Plot[A <: Layout[A]](
    val series: Vector[Series] = Vector.empty,
    val layout: Layout[A] = SingleAxisLayout()
) {

  def layout[B <: Layout[B]](newLayout: Layout[B]): Plot[B] =
    copy(layout = newLayout)

  def withScatter[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y],
      options: ScatterOptions = ScatterOptions()
  ): Plot[A] = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = series :+ Scatter(xsAsPType, ysAsPType, options))
  }

  def withBar[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y]
  ): Plot[A] = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = series :+ Bar(xsAsPType, ysAsPType, BarOptions()))
  }

  def withBox[X: Writable](
    xs: Iterable[X]
  ): Plot[A] = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    copy(series = series :+ Box(xsAsPType, BoxOptions()))
  }

}
