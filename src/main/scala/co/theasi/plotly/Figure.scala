package co.theasi.plotly

trait Figure {
  type Self <: Figure

  def plots: Vector[Plot]
  def viewPorts: Vector[ViewPort]
  def options: FigureOptions

  def withNewOptions(newOptions: FigureOptions): Self

  /** Set legend options
    *
    * {{{
    * val legend = LegendOptions().fontSize(20)
    * val figure = Figure().legend(legend)
    * }}}
    */
  def legend(newLegend: LegendOptions): Self =
    withNewOptions(options.copy(legendOptions = newLegend))

  /** Set figure title */
  def title(newTitle: String): Self =
    withNewOptions(options.copy(title = Some(newTitle)))

  private def margins(newMargins: Margins): Self =
    withNewOptions(options.copy(margins = newMargins))

  /** Set layout margins */
  def margins(top: Int, right: Int, bottom: Int, left: Int): Self =
    margins(Margins(Some(top), Some(right), Some(bottom), Some(left)))

  /** Set left margin (in px) */
  def leftMargin(newLeftMargin: Int): Self = {
    val newMargins = options.margins.copy(left = Some(newLeftMargin))
    margins(newMargins)
  }
  /** Set right margin (in px) */
  def rightMargin(newRightMargin: Int): Self = {
    val newMargins = options.margins.copy(right = Some(newRightMargin))
    margins(newMargins)
  }
  /** Set top margin (in px) */
  def topMargin(newTopMargin: Int): Self = {
    val newMargins = options.margins.copy(top = Some(newTopMargin))
    margins(newMargins)
  }
  /** Set bottom margin (in px) */
  def bottomMargin(newBottomMargin: Int): Self = {
    val newMargins = options.margins.copy(bottom = Some(newBottomMargin))
    margins(newMargins)
  }

  /** Set the plot width (in px) */
  def width(newWidth: Int): Self =
    withNewOptions(options.copy(width = Some(newWidth)))

  /** Set the plot height (in px) */
  def height(newHeight: Int): Self =
    withNewOptions(options.copy(height = Some(newHeight)))

  /** Set the paper's background color
    *
    * {{{
    * val figure = Figure().paperBackgroundColor(Color.rgb(0, 255, 0))
    * }}}
    */
  def paperBackgroundColor(newColor: Color): Self =
    withNewOptions(options.copy(paperBackgroundColor = Some(newColor)))
  /** Set the paper's background color with (red, green, blue, alpha) */
  def paperBackgroundColor(r: Int, g: Int, b: Int, a: Double): Self =
    paperBackgroundColor(Color.rgba(r, g, b, a))
  /** Set the paper's background color. */
  def paperBackgroundColor(r: Int, g: Int, b: Int): Self =
    paperBackgroundColor(Color.rgb(r, g, b))

  /** Set the plot's background color
    *
    * {{{
    * val figure = Figure().plotBackgroundColor(Color.rgb(0, 255, 0))
    * }}}
    */
  def plotBackgroundColor(newColor: Color): Self =
    withNewOptions(options.copy(plotBackgroundColor = Some(newColor)))
  /** Set the plot's background color with (red, green, blue, alpha) */
  def plotBackgroundColor(r: Int, g: Int, b: Int, a: Double): Self =
    plotBackgroundColor(Color.rgba(r, g, b, a))
  /** Set the plot's background color */
  def plotBackgroundColor(r: Int, g: Int, b: Int): Self =
    plotBackgroundColor(Color.rgb(r, g, b))


}

object Figure {
  def apply(): SinglePlotFigure = SinglePlotFigure()
}


case class SinglePlotFigure(plot: Plot, options: FigureOptions)
extends Figure {

  type Self = SinglePlotFigure

  def plots = Vector(plot)
  def viewPorts = Vector(ViewPort((0.0, 1.0), (0.0, 1.0)))

  def plot(newPlot: Plot): SinglePlotFigure = copy(plot = newPlot)

  def withNewOptions(newOptions: FigureOptions): Self =
    copy(options = newOptions)

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

  type Self = GridFigure

  def plot(rowIndex: Int, columnIndex: Int)(newPlot: Plot): GridFigure = {
    val ref = subplotRef(rowIndex, columnIndex)
    copy(plots = plots.updated(ref, newPlot))
  }

  def viewPortAt(rowIndex: Int, columnIndex: Int): ViewPort = {
    val ref = subplotRef(rowIndex, columnIndex)
    viewPorts(ref)
  }

  def withNewOptions(newOptions: FigureOptions): Self =
    copy(options = newOptions)

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
