package co.theasi.plotly

case class Layout(
  val xAxes: Vector[Axis] = Vector.empty,
  val yAxes: Vector[Axis] = Vector.empty
)

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

    // Axis prototypes: each x prototype needs to be matched
    // with each y prototype to form a graph.
    val xAxesProto = xDomains.map { domain => Axis(domain) }
    val yAxesProto = yDomains.map { domain => Axis(domain) }

    // Cartesian product of xAxesProto and yAxesProto
    val axisPairs = for {
      yAxis <- yAxesProto
      xAxis <- xAxesProto
    } yield (xAxis, yAxis)

    val xAxes = axisPairs.map { _._1 }.toVector
    val yAxes = axisPairs.map { _._2 }.toVector

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
