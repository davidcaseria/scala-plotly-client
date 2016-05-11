package co.theasi.plotly

trait Figure {
  def plots: Vector[Plot]
  def viewPorts: Vector[ViewPort]
  def options: FigureOptions
}

object Figure {
  def apply(): SinglePlotFigure = SinglePlotFigure()
}


case class SinglePlotFigure(plot: Plot, options: FigureOptions)
extends Figure {

  def plots = Vector(plot)
  def viewPorts = Vector(ViewPort((0.0, 1.0), (0.0, 1.0)))

  def plot(newPlot: Plot): SinglePlotFigure = copy(plot = newPlot)
}

object SinglePlotFigure {
  def apply(): SinglePlotFigure = apply(FigureOptions())
  def apply(options: FigureOptions): SinglePlotFigure =
    SinglePlotFigure(Plot(), options)
}


case class GridFigure(
    plots: Vector[Plot],
    viewPorts: Vector[ViewPort],
    numberRows: Int,
    numberColumns: Int,
    options: FigureOptions)
extends Figure {

  def plot(rowIndex: Int, columnIndex: Int)(newPlot: Plot): GridFigure = {
    val ref = subplotRef(rowIndex, columnIndex)
    copy(plots = plots.updated(ref, newPlot))
  }

  def viewPortAt(rowIndex: Int, columnIndex: Int): ViewPort = {
    val ref = subplotRef(rowIndex, columnIndex)
    viewPorts(ref)
  }

  private def subplotRef(rowIndex: Int, columnIndex: Int): Int = {
    checkRowColumn(rowIndex, columnIndex)
    rowColumnToRefImpl(rowIndex, columnIndex)
  }

  private def rowColumnToRefImpl(row: Int, column: Int): Int = {
    val value = row*numberColumns + column
    value
  }

  private def checkRowColumn(row: Int, column: Int) {
    checkRow(row)
    checkColumn(column)
  }

  private def checkRow(row: Int) {
    if (row >= numberRows) {
      throw new IllegalArgumentException(s"Row index $row out of bounds.")
    }
  }

  private def checkColumn(column: Int) {
    if (column >= numberColumns) {
      throw new IllegalArgumentException(s"Column index $column out of bounds.")
    }
  }
}


object GridFigure {

  val DefaultHorizontalSpacing = 0.2
  val DefaultVerticalSpacing = 0.3

  def apply(
      numberRows: Int,
      numberColumns: Int,
      options: FigureOptions)
  : GridFigure = {

    // Spacing between plots
    val horizontalSpacing = DefaultHorizontalSpacing / numberColumns.toDouble
    val verticalSpacing = DefaultVerticalSpacing / numberRows.toDouble

    // plot width
    val width =
      (1.0 - horizontalSpacing * (numberColumns - 1))/numberColumns.toDouble

    // plot height
    val height =
      (1.0 - verticalSpacing * (numberRows - 1))/numberRows.toDouble

    val xDomains = (0 until numberColumns).map { icol =>
      val start = icol * (width + horizontalSpacing)
      val end = start + width
      (start, end)
    }

    val yDomains = (0 until numberRows).map { irow =>
      val top = 1.0 - (irow * (height + verticalSpacing))
      val bottom = top - height
      (bottom, top)
    }

    // Cartesian product of xDomains and yDomains
    val viewPorts = for {
      yDomain <- yDomains
      xDomain <- xDomains
    } yield ViewPort(xDomain, yDomain)

    val plots = Vector.fill(viewPorts.size) { Plot() }

    GridFigure(plots, viewPorts.toVector,
      numberRows, numberColumns, options)
  }

  def apply(numberRows: Int, numberColumns: Int): GridFigure =
    apply(numberRows, numberColumns, FigureOptions())
}
