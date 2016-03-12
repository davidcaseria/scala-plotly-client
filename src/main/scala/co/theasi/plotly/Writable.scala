package co.theasi.plotly

trait Writable[T] {
  def toPType(x: T): PType
}

trait WritableImplicits {
  implicit object WritableFloat extends Writable[Float] {
    def toPType(x: Float) = PDouble(x.toDouble)
  }
  implicit object WritableDouble extends Writable[Double] {
    def toPType(x: Double) = PDouble(x)
  }
}
