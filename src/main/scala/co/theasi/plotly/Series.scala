package co.theasi.plotly

sealed trait Series {
  val options: SeriesOptions

  def xsAs[T : Readable]: Iterable[T] = {
    this match {
      case s: Series1D[_] =>
        s.xs.map { implicitly[Readable[T]].fromPType(_) }
      case s: Series2D[_, _] =>
        s.xs.map { implicitly[Readable[T]].fromPType(_) }
    }
  }

  def ysAs[T : Readable]: Iterable[T] = {
    this match {
      case s: Series2D[_, _] =>
        s.xs.map { implicitly[Readable[T]].fromPType(_) }
      case _ =>
        throw new IllegalArgumentException(
          "Cannot extract ys from 1D series")
    }
  }
}

sealed abstract class Series1D[X <: PType] extends Series {
  val xs: Iterable[X]
}

case class Box[X <: PType](
    val xs: Iterable[X],
    val options: BoxOptions)
extends Series1D[X]

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
