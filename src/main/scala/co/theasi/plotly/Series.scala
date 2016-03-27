package co.theasi.plotly

sealed trait Series {
  type OptionType <: SeriesOptions[OptionType]
  val options: OptionType

  def options(newOptions: OptionType): Series

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

sealed abstract class Series1D[
    X <: PType]
extends Series {
  val xs: Iterable[X]
}

case class Box[X <: PType](
    val xs: Iterable[X],
    override val options: BoxOptions)
extends Series1D[X] {
  type OptionType = BoxOptions

  override def options(newOptions: BoxOptions): Box[X] =
    copy(options = newOptions)
}

sealed abstract class Series2D[
    X <: PType,
    Y <: PType]
extends Series {
  val xs: Iterable[X]
  val ys: Iterable[Y]
  val options: OptionType
}

case class Scatter[
    X <: PType,
    Y <: PType](
    val xs: Iterable[X],
    val ys: Iterable[Y],
    override val options: ScatterOptions)
extends Series2D[X, Y] {
  type OptionType = ScatterOptions

  override def options(newOptions: ScatterOptions): Scatter[X, Y] =
    copy(options = newOptions)
}


case class Bar[X <: PType, Y <: PType](
    val xs: Iterable[X],
    val ys: Iterable[Y],
    override val options: BarOptions)
extends Series2D[X, Y] {
  type OptionType = BarOptions

  override def options(newOptions: BarOptions): Bar[X, Y] =
    copy(options = newOptions)
}
