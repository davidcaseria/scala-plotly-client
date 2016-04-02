package co.theasi.plotly

case class Layout(xAxes: Vector[Axis], yAxes: Vector[Axis]) {
  def withXAxisOptions(axisRef: Int, newOptions: AxisOptions): Layout = {
    val newXAxis = xAxes(axisRef).copy(options = newOptions)
    val newXAxes = xAxes.updated(axisRef, newXAxis)
    copy(xAxes = newXAxes)
  }

  def withXAxisOptions(newOptions: AxisOptions): Layout = {
    if(xAxes.size > 1) {
      throw new IllegalStateException(
        "Use the form withXAxisOptions(axisRef, newOptions) if there are more than one x-axes")
    }
    withXAxisOptions(0, newOptions)
  }

  def updateXAxisOptions(
      axisRef: Int,
      updater: AxisOptions => AxisOptions
  ): Layout = {
    val newOptions = updater(xAxes(axisRef).options)
    withXAxisOptions(axisRef, newOptions)
  }

  def withYAxisOptions(axisRef: Int, newOptions: AxisOptions): Layout = {
    val newYAxis = yAxes(axisRef).copy(options = newOptions)
    val newYAxes = yAxes.updated(axisRef, newYAxis)
    copy(yAxes = newYAxes)
  }

  def withYAxisOptions(newOptions: AxisOptions): Layout = {
    if(yAxes.size > 1) {
      throw new IllegalStateException(
        "Use the form withYAxisOptions(axisRef, newOptions) if there are more than one y-axes")
    }
    withYAxisOptions(0, newOptions)
  }

  def updateYAxisOptions(
      axisRef: Int,
      updater: AxisOptions => AxisOptions
  ): Layout = {
    val newOptions = updater(yAxes(axisRef).options)
    withYAxisOptions(axisRef, newOptions)
  }
}

case class SubplotsRef(
  val subplots: Vector[Vector[Int]]
) {
  def apply(row: Int, column: Int): Int = subplots(row)(column)
  def rowRef(row: Int): Vector[Int] =
    subplots(row)
  def columnRef(column: Int): Vector[Int] =
    subplots.map { row => row(column) }
}

object Layout {

  val DefaultHorizontalSpacing = 0.2
  val DefaultVerticalSpacing = 0.3

  def apply(): Layout = {
    val ax = Axis((0.0, 1.0), 0)
    Layout(Vector(ax), Vector(ax))
  }

  def subplots(rows: Int, columns: Int): (Layout, SubplotsRef) = {

    // Spacing between plots
    val horizontalSpacing = DefaultHorizontalSpacing / columns.toDouble
    val verticalSpacing = DefaultVerticalSpacing / rows.toDouble

    // plot width
    val width = (1.0 - horizontalSpacing * (columns - 1))/columns.toDouble

    // plot height
    val height = (1.0 - verticalSpacing * (rows - 1))/rows.toDouble

    val xDomains = (0 until columns).map { icol =>
      val start = icol * (width + horizontalSpacing)
      val end = start + width
      (start, end)
    }

    val yDomains = (0 until rows).map { irow =>
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

    // build SubplotsRef grid
    val subplotsRef = buildSubplotsRef(rows, columns)

    (Layout(xAxes, yAxes), subplotsRef)
  }

  private def buildSubplotsRef(rows: Int, columns: Int): SubplotsRef = {
    var counter = 0
    val grid = (0 until rows).map { xAxisRef =>
      (0 until columns).map { yAxisRef =>
        val oldCounter = counter
        counter += 1
        oldCounter
      }.toVector
    }.toVector
    SubplotsRef(grid)
  }

}
