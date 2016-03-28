package co.theasi.plotly

trait Writable[T] {
  def toPType(x: T): PType
}

trait WritableImplicits {
  implicit object WritableDouble extends Writable[Double] {
    def toPType(x: Double) = PDouble(x)
  }
  implicit object WritableInt extends Writable[Int] {
    def toPType(x: Int) = PInt(x)
  }
  implicit object WritableString extends Writable[String] {
    def toPType(x: String) = PString(x)
  }
}
