package co.theasi.plotly

trait Figure {
  def subplots: Vector[Plot]
}

object Figure {
  def apply(): SinglePlotFigure = SinglePlotFigure()
}


case class SinglePlotFigure(plot: Plot) extends Figure {

  def subplots = Vector(plot)

  def plot(newPlot: Plot): SinglePlotFigure = copy(plot = newPlot)
}

object SinglePlotFigure {
  def apply(): SinglePlotFigure = SinglePlotFigure(Plot())
}
