package co.theasi.plotly

import org.scalatest._

class LayoutSpec extends FlatSpec with Matchers with Inside {

  "GridLayout" should "create subplots in a row" in {
    val l = GridLayout(1, 2)
    val hspace = GridLayout.DefaultHorizontalSpacing / 2.0
    val vspace = GridLayout.DefaultVerticalSpacing / 1.0
    l.xAxes.size shouldEqual 2
    l.yAxes.size shouldEqual 2
    l.xAxes(0).domain shouldEqual((0.0, 0.5 - 0.5*hspace))
    l.xAxes(1).domain shouldEqual((0.5 + 0.5*hspace, 1.0))
    //l.yAxes.map { ax => ax.domain shouldEqual((0.0, 1.0)) }
  }

  it should "create subplots in a column" in {
    val l = GridLayout(2, 1)
    val hspace = GridLayout.DefaultHorizontalSpacing / 1.0
    val vspace = GridLayout.DefaultVerticalSpacing / 2.0
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
    val l = GridLayout(4, 5) // 4 rows, 5 columns
    val hspace = GridLayout.DefaultHorizontalSpacing / 4.0
    val vspace = GridLayout.DefaultVerticalSpacing / 5.0

    l.xAxes.size shouldEqual 20
    l.yAxes.size shouldEqual 20

    // Check xAxes span each row
    (0 until 4).foreach { irow =>
      l.xAxis(irow, 0).domain._1 shouldEqual 0.0
      l.xAxis(irow, 4).domain._2 shouldEqual 1.0
    }

    // Check yAxes span each column
    (0 until 5).foreach { icol =>
      l.yAxis(0, icol).domain._2 shouldEqual 1.0 +- 1e-5
      l.yAxis(3, icol).domain._1 shouldEqual 0.0 +- 1e-5
    }

    def size(ax: Axis) = ax.domain._2 - ax.domain._1

    // Check xAxes all have same width
    val width = size(l.xAxes(0))
    l.xAxes.foreach { ax => size(ax) shouldEqual width +- 1e-5 }

    val height = size(l.yAxes(0))
    l.yAxes.foreach { ax => size(ax) shouldEqual height +- 1e-5 }

  }

  "SingleAxisLayout" should "have the correct domain and anchor" in {
    val l = SingleAxisLayout()
    l.xAxes.size shouldEqual 1
    l.yAxes.size shouldEqual 1
    l.xAxes(0).domain shouldEqual (0.0, 1.0)
    l.yAxes(0).domain shouldEqual (0.0, 1.0)
    l.xAxes(0).anchor shouldEqual 0
    l.yAxes(0).anchor shouldEqual 0
  }

  it should "allow adding x-axis options" in {
    val l = SingleAxisLayout().xAxisOptions(AxisOptions().title("hello"))
    l.xAxes(0).options.title shouldEqual Some("hello")
  }

  it should "allow adding y-axis options" in {
    val l = SingleAxisLayout().yAxisOptions(AxisOptions().title("hello"))
    l.yAxes(0).options.title shouldEqual Some("hello")
  }



}
