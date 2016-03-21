package co.theasi.plotly

sealed trait Series {

}

sealed abstract class Series2D[X <: PType, Y <: PType] extends Series {
  val xs: Iterable[X]
  val ys: Iterable[Y]
  val options: SeriesOptions

  def xsAs[T : Readable]: Iterable[T] = {
    xs.map { implicitly[Readable[T]].fromPType(_) }
  }

  def ysAs[T : Readable]: Iterable[T] = {
    ys.map { implicitly[Readable[T]].fromPType(_) }
  }
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
