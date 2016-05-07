package co.theasi.plotly.writer

// import co.theasi.plotly.{Plot, Grid}

trait ServerWriterWithDefaultCredentials extends ServerWriter {
  implicit val server = ServerWithDefaultCredentials
}

trait ServerWriter {
  implicit val server: Server

  // def draw(
  //     plot: Plot[_],
  //     fileName: String)
  //     (implicit server: Server)
  // : PlotFile = {
  //   draw(plot, fileName, FileOptions())
  // }

  // def draw(
  //     plot: Plot[_],
  //     fileName: String,
  //     fileOptions: FileOptions)
  //     (implicit server: Server)
  // : PlotFile = {
  //   PlotWriter.draw(plot, fileName, fileOptions)
  // }

  // def draw(
  //     grid: Grid,
  //     fileName: String)
  //     (implicit server: Server)
  // : GridFile = {
  //   draw(grid, fileName, FileOptions())
  // }

  // def draw(
  //     grid: Grid,
  //     fileName: String,
  //     fileOptions: FileOptions)
  //     (implicit server: Server)
  // : GridFile = {
  //   GridWriter.draw(grid, fileName, fileOptions)
  // }



}
