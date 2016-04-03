package co.theasi.plotly

sealed trait Layout {
  val xAxes: Vector[Axis]
  val yAxes: Vector[Axis]

  protected def xAxisOptionsImpl(
      axisRef: Int,
      newOptions: AxisOptions)
  : Vector[Axis] = {
    val newXAxis = xAxes(axisRef).copy(options = newOptions)
    xAxes.updated(axisRef, newXAxis)
  }

  protected def yAxisOptionsImpl(
      axisRef: Int,
      newOptions: AxisOptions)
  : Vector[Axis] = {
    val newYAxis = yAxes(axisRef).copy(options = newOptions)
    yAxes.updated(axisRef, newYAxis)
  }
}


case class SingleAxisLayout(xAxis: Axis, yAxis: Axis)
extends Layout {
  val xAxes = Vector(xAxis)
  val yAxes = Vector(yAxis)

  def xAxisOptions(newOptions: AxisOptions): SingleAxisLayout = {
    copy(xAxis = xAxis.copy(options = newOptions))
  }

  def yAxisOptions(newOptions: AxisOptions): SingleAxisLayout = {
    copy(yAxis = yAxis.copy(options = newOptions))
  }
}


object SingleAxisLayout {
  def apply(): SingleAxisLayout =
    SingleAxisLayout(Axis(), Axis())
}


case class GridLayout(
    xAxes: Vector[Axis],
    yAxes: Vector[Axis],
    numberRows: Int,
    numberColumns: Int)
extends Layout {

  def xAxis(row: Int, column: Int) = xAxes(ref(row, column)._1)
  def yAxis(row: Int, column: Int) = yAxes(ref(row, column)._2)

  def xAxisOptions(row: Int, column: Int, newOptions: AxisOptions) = {
    val axisRef = ref(row, column)._1
    copy(xAxes = xAxisOptionsImpl(axisRef, newOptions))
  }

  def yAxisOptions(row: Int, column: Int, newOptions: AxisOptions) = {
    val axisRef = ref(row, column)._2
    copy(yAxes = yAxisOptionsImpl(axisRef, newOptions))
  }

  def ref(row: Int, column: Int): (Int, Int) = {
    checkRowColumn(row, column)
    rowColumnToRefImpl(row, column)
  }

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

    GridLayout(xAxes, yAxes, numberRows, numberColumns)
  }
}


case class FlexibleLayout(xAxes: Vector[Axis], yAxes: Vector[Axis])
extends Layout {}
