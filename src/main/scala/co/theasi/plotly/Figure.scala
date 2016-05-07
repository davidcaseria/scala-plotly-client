package co.theasi.plotly

trait Figure {
  def subplots: Vector[Subplot]
}

object Figure {
  def apply(): SinglePlotFigure = SinglePlotFigure()
}


case class SinglePlotFigure(subplot: Subplot) extends Figure {

  def subplots = Vector(subplot)

  def plot(newSubplot: Subplot): SinglePlotFigure = copy(subplot = newSubplot)
}

object SinglePlotFigure {
  def apply(): SinglePlotFigure = SinglePlotFigure(CartesianSubplot())
}
