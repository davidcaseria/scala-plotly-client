package co.theasi.plotly

case class Layout(
  val xAxes: Vector[Axis] = Vector.empty,
  val yAxes: Vector[Axis] = Vector.empty
)

object Layout {

  val DefaultHorizontalSpacing = 0.2
  val DefaultVerticalSpacing = 0.3

  def subplots(rows: Int, columns: Int): Layout = {

    // Spacing between plots
    val horizontalSpacing = DefaultHorizontalSpacing / columns.toDouble
    val verticalSpacing = DefaultVerticalSpacing / rows.toDouble

    // plot width
    val width = (1.0 - horizontalSpacing * (columns - 1))/columns.toDouble
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

    val xAxesProto = xDomains.map { domain => Axis(domain) }
    val yAxesProto = yDomains.map { domain => Axis(domain) }

    val axisPairs = for {
      yAxis <- yAxesProto
      xAxis <- xAxesProto
    } yield (xAxis, yAxis)

    val xAxes = axisPairs.map { _._1 }.toVector
    val yAxes = axisPairs.map { _._2 }.toVector

    Layout(xAxes, yAxes)
  }

}
