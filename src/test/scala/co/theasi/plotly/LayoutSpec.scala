package co.theasi.plotly

import org.scalatest._

class LayoutSpec extends FlatSpec with Matchers with Inside {

  "Layout.subplots" should "create subplots in a row" in {
    val (l, subplots) = Layout.subplots(1, 2)
    val hspace = Layout.DefaultHorizontalSpacing / 2.0
    val vspace = Layout.DefaultVerticalSpacing / 1.0
    l.xAxes.size shouldEqual 2
    l.yAxes.size shouldEqual 2
    l.xAxes(0).domain shouldEqual((0.0, 0.5 - 0.5*hspace))
    l.xAxes(1).domain shouldEqual((0.5 + 0.5*hspace, 1.0))
    //l.yAxes.map { ax => ax.domain shouldEqual((0.0, 1.0)) }
  }

  it should "create subplots in a column" in {
    val (l, subplots) = Layout.subplots(2, 1)
    val hspace = Layout.DefaultHorizontalSpacing / 1.0
    val vspace = Layout.DefaultVerticalSpacing / 2.0
    l.xAxes.size shouldEqual 2
    l.yAxes.size shouldEqual 2
    l.xAxes.map { ax => ax.domain shouldEqual((0.0, 1.0)) }
    l.yAxes(0).domain shouldEqual((0.5 + 0.5*vspace, 1.0))

    // FPE occurs for yAxes(1) -> compare using approximate equality
    inside(l.yAxes(1).domain) { case (bottom, top) =>
      bottom shouldEqual 0.0 +- 1e-5
      top shouldEqual(0.5 - 0.5*vspace) +- 1e-5
    }
  }

  it should "create subplots in a grid" in {
    val (l, subplots) = Layout.subplots(4, 5) // 4 rows, 5 columns
    val hspace = Layout.DefaultHorizontalSpacing / 4.0
    val vspace = Layout.DefaultVerticalSpacing / 5.0

    l.xAxes.size shouldEqual 20
    l.yAxes.size shouldEqual 20

    // Check xAxes span each row
    (0 until 4).foreach { irow =>
      val row = subplots.rowRef(irow)
      l.xAxes(row(0)).domain._1 shouldEqual 0.0
      l.xAxes(row(4)).domain._2 shouldEqual 1.0
    }

    // Check yAxes span each column
    (0 until 5).foreach { icol =>
      val col = subplots.columnRef(icol)
      l.yAxes(col(0)).domain._2 shouldEqual 1.0 +- 1e-5
      l.yAxes(col(3)).domain._1 shouldEqual 0.0 +- 1e-5
    }

    def size(ax: Axis) = ax.domain._2 - ax.domain._1

    // Check xAxes all have same width
    val width = size(l.xAxes(0))
    l.xAxes.foreach { ax => size(ax) shouldEqual width +- 1e-5 }

    val height = size(l.yAxes(0))
    l.yAxes.foreach { ax => size(ax) shouldEqual height +- 1e-5 }

  }


}
