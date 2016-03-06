package co.theasi.plotly

trait Writable[T] {
  def toPlotlyString(x: T): String = x.toString
}

trait WritableImplicits {
  implicit object WritableFloat extends Writable[Float]
  implicit object WritableDouble extends Writable[Double]
  implicit object WritableInt extends Writable[Int]
}
