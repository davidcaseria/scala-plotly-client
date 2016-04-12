package co.theasi.plotly

/** Base class for plot layouts.
  *
  * Do not construct this class directly. Use one of the sub-classes instead:
  *
  *  - [[SingleAxisLayout]] to have a single pair of axes on the figure
  *  - [[RowLayout]] for subplots equally distributed in a single line
  *  - [[GridLayout]] for subplots distributed on a grid.
  */
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

/** Layout with a single plot.
  *
  * ==Example usage==
  *
  * {{{
  * val xs = Vector(1, 2, 3)
  * val ys = Vector(4.5, 8.5, 21.0)
  *
  * val layout = SingleAxisLayout()
  *   .leftMargin(140)
  *   .paperBackgroundColor(254, 247, 234)
  *   .plotBackgroundColor(254, 247, 234)
  *   .xAxisOptions(AxisOptions()
  *     .titleColor(204, 204, 204)
  *     .noAutoTick)
  *
  * val plot = Plot().layout(layout).withScatter(xs, ys)
  * }}}
  */
case class SingleAxisLayout(xAxis: Axis, yAxis: Axis, options: LayoutOptions)
extends Layout[SingleAxisLayout] {
  def xAxes = Vector(xAxis)
  def yAxes = Vector(yAxis)

  /** Set options for the x-axis */
  def xAxisOptions(newOptions: AxisOptions): SingleAxisLayout =
    copy(xAxis = xAxis.copy(options = newOptions))

  /** Set options for the y-axis */
  def yAxisOptions(newOptions: AxisOptions): SingleAxisLayout =
    copy(yAxis = yAxis.copy(options = newOptions))

  def newOptions(newOptions: LayoutOptions): SingleAxisLayout =
    copy(options = newOptions)
}


object SingleAxisLayout {
  def apply(): SingleAxisLayout =
    SingleAxisLayout(Axis(), Axis(), LayoutOptions())
}


/** Layout with a grid of sub-plots.
  *
  * This creates a grid of subplots arranged in rows and columns.
  *
  * ==Example Usage==
  *
  * Let's draw six scatter plots on a grid.
  *
  * {{{
  * import util.Random
  *
  * val xs = (0 to 100).map { i => Random.nextGaussian }
  * def ys = (0 to 100).map { i => Random.nextGaussian }
  *
  * // 2 rows, 3 columns
  * val layout = GridLayout(2, 3)
  * }}}
  *
  * To create a new plot with this layout, we use the [[Plot.layout]] method:
  *
  * {{{
  * val p = Plot().layout(layout)
  * }}}
  *
  * We can specify which subplot a new data series will be plotted to using
  * the 'onAxes' series option.
  *
  * {{{
  * val p = Plot()
  *   .layout(layout)
  *   .withScatter(xs, ys, ScatterOptions().onAxes(layout.ref(0, 1))) // top middle
  * }}}
  *
  * The `.ref` method takes a (row, column) pair as arguments and returns a single
  * axis index that can be passed to `.onAxes`.
  *
  */
case class GridLayout(
    xAxes: Vector[Axis],
    yAxes: Vector[Axis],
    numberRows: Int,
    numberColumns: Int,
    options: LayoutOptions
) extends Layout[GridLayout] {

  def xAxis(row: Int, column: Int) = xAxes(ref(row, column)._1)
  def yAxis(row: Int, column: Int) = yAxes(ref(row, column)._2)

  /** Set new axis options for the x-axis of one of the subplots. */
  def xAxisOptions(row: Int, column: Int, newOptions: AxisOptions)
  : GridLayout = {
    val axisRef = ref(row, column)._1
    val newAxis = xAxes(axisRef).copy(options = newOptions)
    copy(xAxes = xAxes.updated(axisRef, newAxis))
  }

  /** Set new axis options for the y-axis of one of the subplots. */
  def yAxisOptions(row: Int, column: Int, newOptions: AxisOptions) = {
    val axisRef = ref(row, column)._2
    val newAxis = yAxes(axisRef).copy(options = newOptions)
    copy(yAxes = yAxes.updated(axisRef, newAxis))
  }

  /** Returns the index of axes at (row, column)
    *
    * The main use case for this is to generate an argument
    * for the `.onAxes` method, to specify which sub-plot a
    * new series should be drawn on.
    *
    * @example {{{
    * // grid layout with 2 rows and 3 columns
    * val layout = GridLayout(2, 3)
    *
    * // options for plotting on subplot (0, 1): top row, middle column
    * val options = ScatterOptions().onAxes(layout.ref(0, 1))
    * }}}
    */
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


/** Layout with a row of subplots.
  *
  * ==Example Usage==
  *
  * Let's draw 2 scatter plots.
  * {{{
  * import util.Random
  *
  * val xsLeft = (0 to 100).map { i => Random.nextGaussian }
  * val ysLeft = (0 to 100).map { i => Random.nextGaussian }
  *
  * val xsRight = (0 to 100).map { i => Random.nextDouble }
  * val ysRight = (0 to 100).map { i => Random.nextDouble }
  *
  * // 2 subplots
  * val layout = RowLayout(2)
  * }}}
  *
  * To create a new plot with this layout, we use the [[Plot.layout]] method:
  *
  * {{{
  * val p = Plot().layout(layout)
  * }}}
  * We can specify which subplot a new data series will be plotted to using the 'onAxes'
  * series option.
  *
  * {{{
  * val p = Plot().layout(layout)
  *   .withScatter(xsLeft, ysLeft,
  *     ScatterOptions()
  *       .onAxes(layout.ref(0)) // left subplot
  *       .mode(ScatterMode.Marker))
  *   .withScatter(xsRight, ysRight,
  *     ScatterOptions()
  *       .onAxes(layout.ref(1)) // right subplot
  *       .mode(ScatterMode.Marker))
  * }}}
  *
  * The `.ref` method takes a single index denoting which subplot to plot on, and
  * returns an index that can be passed to `.onAxes`.
  */
case class RowLayout(private val impl: GridLayout)
extends Layout[RowLayout] {
  // The implementation is just a wrapper around a GridLayout with
  // a single row.
  def xAxes = impl.xAxes
  def yAxes = impl.yAxes
  val options = impl.options

  /** Returns the index of the axes on a subplot
    *
    * The main use case for this is to generate an argument
    * for the `.onAxes` method, to specify which sub-plot a
    * new series should be drawn on.
    *
    * @example {{{
    * // Layout with 3 subplots
    * val layout = RowLayout(3)
    *
    * // options for plotting on subplot (1): middle subplot
    * val options = ScatterOptions().onAxes(layout.ref(1))
    * }}}
    */
  def ref(subplot: Int): (Int, Int) = impl.ref(0, subplot)

  /** Set new axis options for the x-axis of one of the subplots. */
  def xAxisOptions(subplot: Int, newOptions: AxisOptions): RowLayout =
    copy(impl.xAxisOptions(0, subplot, newOptions))

  /** Set new axis options for the y-axis of one of the subplots. */
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
