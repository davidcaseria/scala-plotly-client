package co.theasi

import co.theasi.plotly.PlotWriter

package object plotly {
  implicit val session = new DefaultSession

  def plot(
      x: Iterable[Double],
      y: Iterable[Double],
      fileName: String)(
      implicit session: Session) = PlotWriter.plot(x, y, fileName)

  def plotAsync(
      x: Iterable[Double],
      y: Iterable[Double],
      fileName: String)(
      implicit session: Session) =
    PlotWriter.plotAsync(x, y, fileName)
}
