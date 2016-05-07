package co.theasi.plotly

trait Subplot {
  def series: Vector[Series]
}


case class CartesianSubplot(series: Vector[Series]) extends Subplot {

  def withScatter[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y],
      options: ScatterOptions = ScatterOptions()
  ): CartesianSubplot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = series :+ Scatter(xsAsPType, ysAsPType, options))
  }

}


object CartesianSubplot {

  def apply(): CartesianSubplot = CartesianSubplot(Vector.empty[Series])

}

