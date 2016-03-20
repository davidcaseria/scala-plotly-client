package co.theasi

package object plotly extends WritableImplicits {

  implicit val server = ServerWithDefaultCredentials

  def draw(
      plot: Plot,
      fileName: String)
      (implicit server: Server)
  : PlotFile = {
    draw(plot, fileName, FileOptions())
  }

  def draw(
      plot: Plot,
      fileName: String,
      fileOptions: FileOptions)
      (implicit server: Server)
  : PlotFile = {
    PlotWriter.draw(plot, fileName, fileOptions)
  }

  def draw(
      grid: Grid,
      fileName: String)
      (implicit server: Server)
  : GridFile = {
    draw(grid, fileName, FileOptions())
  }

  def draw(
      grid: Grid,
      fileName: String,
      fileOptions: FileOptions)
      (implicit server: Server)
  : GridFile = {
    GridWriter.draw(grid, fileName, fileOptions)
  }

  sealed trait PType
  case class PString(s: String) extends PType
  case class PInt(i: Int) extends PType
  case class PDouble(d: Double) extends PType

}
