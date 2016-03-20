package co.theasi.plotly

sealed trait Series {

}

sealed abstract class Series2D[X <: PType, Y <: PType] extends Series {
  val xs: Iterable[X]
  val ys: Iterable[Y]
  val options: SeriesOptions
}

case class Scatter[X <: PType, Y <: PType](
    val xs: Iterable[X],
    val ys: Iterable[Y],
    override val options: ScatterOptions)
extends Series2D[X, Y]


case class Bar[X <: PType, Y <: PType](
    val xs: Iterable[X],
    val ys: Iterable[Y],
    override val options: BarOptions)
extends Series2D[X, Y]
