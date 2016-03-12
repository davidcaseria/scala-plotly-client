package co.theasi.plotly

sealed trait Series {

}

sealed abstract class Series2D[X <: PType, Y <: PType] extends Series {
  val xs: Iterable[X]
  val ys: Iterable[Y]
}

case class Scatter[X <: PType, Y <: PType](
    val xs: Iterable[X],
    val ys: Iterable[Y])
extends Series2D[X, Y]
