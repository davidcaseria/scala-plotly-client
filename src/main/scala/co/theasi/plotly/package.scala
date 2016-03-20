package co.theasi

package object plotly extends WritableImplicits {

  implicit val server = ServerWithDefaultCredentials

  def draw(
      plot: Plot,
      fileName: String)
      (implicit server: Server)
  : DrawnPlot = {
    draw(plot, fileName, FileOptions())
  }

  def draw(
      plot: Plot,
      fileName: String,
      fileOptions: FileOptions)
      (implicit server: Server)
  : DrawnPlot = {
    PlotWriter.draw(plot, fileName, fileOptions)
  }

  def draw(
      grid: Grid,
      fileName: String)
      (implicit server: Server)
  : DrawnGrid = {
    draw(grid, fileName, FileOptions())
  }

  def draw(
      grid: Grid,
      fileName: String,
      fileOptions: FileOptions)
      (implicit server: Server)
  : DrawnGrid = {
    GridWriter.draw(grid, fileName, fileOptions)
  }

  sealed trait PType
  case class PString(s: String) extends PType
  case class PInt(i: Int) extends PType
  case class PDouble(d: Double) extends PType

}
