package co.theasi

package object plotly extends WritableImplicits {

  implicit val api = new ApiWithDefaultCredentials {}

  sealed trait PType
  case class PString(s: String) extends PType
  case class PInt(i: Int) extends PType
  case class PDouble(d: Double) extends PType

}
