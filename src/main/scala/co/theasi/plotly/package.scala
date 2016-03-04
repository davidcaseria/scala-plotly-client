package co.theasi

import co.theasi.plotly.PlotWriter

package object plotly {
  def plot(x: Iterable[Double], y: Iterable[Double], fileName: String) =
    PlotWriter.plot(x, y, fileName)

  def plotAsync(x: Iterable[Double], y: Iterable[Double], fileName: String) =
    PlotWriter.plotAsync(x, y, fileName)
}
