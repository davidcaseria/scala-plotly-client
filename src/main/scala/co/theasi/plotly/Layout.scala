package co.theasi.plotly

sealed trait Layout[A <: Layout[A]] {
  def xAxes: Vector[Axis]
  def yAxes: Vector[Axis]
  val options: LayoutOptions

  def newOptions(newOptions: LayoutOptions): A

  /** Set layout title */
  def title(newTitle: String): A =
    newOptions(options.copy(title = Some(newTitle)))

  /** Set layout legend options
    *
    * {{{
    * val legend = LegendOptions().fontSize(20)
    * val layout = SingleAxisLayout().legendOptions(legend)
    * }}}
    */
  def legend(newLegend: LegendOptions): A =
    newOptions(options.copy(legendOptions = newLegend))

  private def margins(newMargins: Margins): A =
    newOptions(options.copy(margins = newMargins))

  /** Set layout margins */
  def margins(top: Int, right: Int, bottom: Int, left: Int): A =
    margins(Margins(Some(top), Some(right), Some(bottom), Some(left)))
  /** Set left margin (in px) */
  def leftMargin(newLeftMargin: Int): A = {
    val newMargin = options.margins.copy(left = Some(newLeftMargin))
    margins(newMargin)
  }
  /** Set right margin (in px) */
  def rightMargin(newRightMargin: Int): A = {
    val newMargin = options.margins.copy(right = Some(newRightMargin))
    margins(newMargin)
  }
  /** Set top margin (in px) */
  def topMargin(newTopMargin: Int): A = {
    val newMargin = options.margins.copy(top = Some(newTopMargin))
    margins(newMargin)
  }
  /** Set bottom margin (in px) */
  def bottomMargin(newBottomMargin: Int): A = {
    val newMargin = options.margins.copy(bottom = Some(newBottomMargin))
    margins(newMargin)
  }

  /** Set the plot width (in px) */
  def width(newWidth: Int): A = newOptions(options.copy(width = Some(newWidth)))

  /** Set the plot height (in px) */
  def height(newHeight: Int): A =
    newOptions(options.copy(height = Some(newHeight)))

  /** Set the paper's background color
    *
    * {{{
    * val layout = SingleAxisLayout().paperBackgroundColor(Color.rgb(0, 255, 0))
    * }}}
    */
  def paperBackgroundColor(newColor: Color): A =
    newOptions(options.copy(paperBackgroundColor = Some(newColor)))
  /** Set the paper's background color with (red, green, blue, alpha) */
  def paperBackgroundColor(r: Int, g: Int, b: Int, a: Double): A =
    paperBackgroundColor(Color.rgba(r, g, b, a))
  /** Set the paper's background color. */
  def paperBackgroundColor(r: Int, g: Int, b: Int): A =
    paperBackgroundColor(Color.rgb(r, g, b))

  /** Set the plot's background color
    *
    * {{{
    * val layout = SingleAxisLayout().plotBackgroundColor(Color.rgb(0, 255, 0))
    * }}}
    */
  def plotBackgroundColor(newColor: Color): A =
    newOptions(options.copy(plotBackgroundColor = Some(newColor)))
  /** Set the plot's background color with (red, green, blue, alpha) */
  def plotBackgroundColor(r: Int, g: Int, b: Int, a: Double): A =
    plotBackgroundColor(Color.rgba(r, g, b, a))
  /** Set the plot's background color */
  def plotBackgroundColor(r: Int, g: Int, b: Int): A =
    plotBackgroundColor(Color.rgb(r, g, b))

}


case class SingleAxisLayout(xAxis: Axis, yAxis: Axis, options: LayoutOptions)
extends Layout[SingleAxisLayout] {
  def xAxes = Vector(xAxis)
  def yAxes = Vector(yAxis)

  def xAxisOptions(newOptions: AxisOptions): SingleAxisLayout =
    copy(xAxis = xAxis.copy(options = newOptions))

  def yAxisOptions(newOptions: AxisOptions): SingleAxisLayout =
    copy(yAxis = yAxis.copy(options = newOptions))

  def newOptions(newOptions: LayoutOptions): SingleAxisLayout =
    copy(options = newOptions)
}


object SingleAxisLayout {
  def apply(): SingleAxisLayout =
    SingleAxisLayout(Axis(), Axis(), LayoutOptions())
}


case class GridLayout(
    xAxes: Vector[Axis],
    yAxes: Vector[Axis],
    numberRows: Int,
    numberColumns: Int,
    options: LayoutOptions
) extends Layout[GridLayout] {

  def xAxis(row: Int, column: Int) = xAxes(ref(row, column)._1)
  def yAxis(row: Int, column: Int) = yAxes(ref(row, column)._2)

  def xAxisOptions(row: Int, column: Int, newOptions: AxisOptions)
  : GridLayout = {
    val axisRef = ref(row, column)._1
    val newAxis = xAxes(axisRef).copy(options = newOptions)
    copy(xAxes = xAxes.updated(axisRef, newAxis))
  }

  def yAxisOptions(row: Int, column: Int, newOptions: AxisOptions) = {
    val axisRef = ref(row, column)._2
    val newAxis = yAxes(axisRef).copy(options = newOptions)
    copy(yAxes = yAxes.updated(axisRef, newAxis))
  }

  def ref(row: Int, column: Int): (Int, Int) = {
    checkRowColumn(row, column)
    rowColumnToRefImpl(row, column)
  }

  def newOptions(newOptions: LayoutOptions): GridLayout =
    copy(options = newOptions)

  private def rowColumnToRefImpl(row: Int, column: Int): (Int, Int) = {
    val value = row*numberColumns + column
    (value, value)
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


object GridLayout {

  val DefaultHorizontalSpacing = 0.2
  val DefaultVerticalSpacing = 0.3

  def apply(numberRows: Int, numberColumns: Int): GridLayout = {

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

    // Cartesian product of xAxesProto and yAxesProto
    val domainPairs = for {
      yDomain <- yDomains
      xDomain <- xDomains
    } yield (xDomain, yDomain)

    val xAxes = domainPairs.zipWithIndex.map {
      case (domainPair, index) => Axis(domainPair._1, index)
    }.toVector
    val yAxes = domainPairs.zipWithIndex.map {
      case (domainPair, index) => Axis(domainPair._2, index)
    }.toVector

    GridLayout(xAxes, yAxes, numberRows, numberColumns, LayoutOptions())
  }
}


case class RowLayout(private val impl: GridLayout)
extends Layout[RowLayout] {
  // The implementation is just a wrapper around a GridLayout with
  // a single row.
  def xAxes = impl.xAxes
  def yAxes = impl.yAxes
  val options = impl.options

  def ref(subplot: Int): (Int, Int) = impl.ref(0, subplot)

  def xAxisOptions(subplot: Int, newOptions: AxisOptions): RowLayout =
    copy(impl.xAxisOptions(0, subplot, newOptions))

  def yAxisOptions(subplot: Int, newOptions: AxisOptions): RowLayout =
    copy(impl.yAxisOptions(0, subplot, newOptions))

  def newOptions(newOptions: LayoutOptions): RowLayout =
    copy(impl.newOptions(newOptions))

}


object RowLayout {
  def apply(numberPlots: Int): RowLayout =
    RowLayout(GridLayout(1, numberPlots))
}

// case class FlexibleLayout(xAxes: Vector[Axis], yAxes: Vector[Axis])
// extends Layout[FlexibleLayout] {}
