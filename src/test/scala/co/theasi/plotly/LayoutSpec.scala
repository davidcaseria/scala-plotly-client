package co.theasi.plotly

import org.scalatest._

class FigureSpec extends FlatSpec with Matchers with Inside {

  def xSize(viewPort: ViewPort) =
    viewPort.xDomain._2 - viewPort.xDomain._1

  def ySize(viewPort: ViewPort) =
    viewPort.yDomain._2 - viewPort.yDomain._1


  "GridFigure" should "create subplots in a row" in {
    val f = GridFigure(1, 2)
    val hspace = GridFigure.DefaultHorizontalSpacing / 2.0
    val vspace = GridFigure.DefaultVerticalSpacing / 1.0
    f.viewPorts.size shouldEqual 2
    f.viewPorts(0).xDomain shouldEqual((0.0, 0.5 - 0.5*hspace))
    f.viewPorts(1).xDomain shouldEqual((0.5 + 0.5*hspace, 1.0))
    f.viewPorts.map { vp => vp.yDomain shouldEqual((0.0, 1.0)) }
  }

  it should "create subplots in a column" in {
    val f = GridFigure(2, 1)
    val hspace = GridFigure.DefaultHorizontalSpacing / 1.0
    val vspace = GridFigure.DefaultVerticalSpacing / 2.0
    f.viewPorts.size shouldEqual 2
    f.viewPorts.map { vp => vp.xDomain shouldEqual((0.0, 1.0)) }
    f.viewPorts(0).yDomain shouldEqual((0.5 + 0.5*vspace, 1.0))

    // FPE occurs for yAxes(1) -> compare using approximate equality
    inside(f.viewPorts(1).yDomain) { case (bottom, top) =>
      bottom shouldEqual 0.0 +- 1e-5
      top shouldEqual(0.5 - 0.5*vspace) +- 1e-5
    }
  }

  it should "create subplots in a grid" in {
    val f = GridFigure(4, 5) // 4 rows, 5 columns
    val hspace = GridFigure.DefaultHorizontalSpacing / 4.0
    val vspace = GridFigure.DefaultVerticalSpacing / 5.0

    f.viewPorts.size shouldEqual 20

    // Check xAxes span each row
    (0 until 4).foreach { irow =>
      f.viewPortAt(irow, 0).xDomain._1 shouldEqual 0.0
      f.viewPortAt(irow, 4).xDomain._2 shouldEqual 1.0
    }

    // Check yAxes span each column
    (0 until 5).foreach { icol =>
      f.viewPortAt(0, icol).yDomain._2 shouldEqual 1.0 +- 1e-5
      f.viewPortAt(3, icol).yDomain._1 shouldEqual 0.0 +- 1e-5
    }

    // Check xAxes all have same width
    val width = xSize(f.viewPorts(0))
    f.viewPorts.foreach { vp => xSize(vp) shouldEqual width +- 1e-5 }

    val height = ySize(f.viewPorts(0))
    f.viewPorts.foreach { vp => ySize(vp) shouldEqual height +- 1e-5 }

  }

//   "SingleAxisLayout" should "have the correct domain and anchor" in {
//     val l = SingleAxisLayout()
//     l.xAxes.size shouldEqual 1
//     l.yAxes.size shouldEqual 1
//     l.xAxes(0).domain shouldEqual (0.0, 1.0)
//     l.yAxes(0).domain shouldEqual (0.0, 1.0)
//     l.xAxes(0).anchor shouldEqual 0
//     l.yAxes(0).anchor shouldEqual 0
//   }

//   it should "allow adding x-axis options" in {
//     val l = SingleAxisLayout().xAxisOptions(AxisOptions().title("hello"))
//     l.xAxes(0).options.title shouldEqual Some("hello")
//   }

//   it should "allow adding y-axis options" in {
//     val l = SingleAxisLayout().yAxisOptions(AxisOptions().title("hello"))
//     l.yAxes(0).options.title shouldEqual Some("hello")
//   }

//   "RowLayout" should "put subplots in a row" in {
//     val l = RowLayout(3)
//     l.yAxes.foreach { axis => axis.domain shouldEqual (0.0, 1.0) }

//     l.xAxes(0).domain._1 shouldEqual 0.0
//     l.xAxes(2).domain._2 shouldEqual 1.0

//     val width = size(l.xAxes(0))
//     l.xAxes.foreach { axis => size(axis) shouldEqual width +- 1e-5 }
//   }

//   it should "allow setting x-axis options" in {
//     val l = RowLayout(3)
//       .xAxisOptions(1, AxisOptions().title("hello"))
//       .xAxisOptions(2, AxisOptions().title("hello2"))

//     l.xAxes(0).options.title shouldEqual None
//     l.xAxes(1).options.title shouldEqual Some("hello")
//     l.xAxes(2).options.title shouldEqual Some("hello2")
//   }

//   it should "allow setting y-axis options" in {
//     val l = RowLayout(3)
//       .yAxisOptions(1, AxisOptions().title("hello"))
//       .yAxisOptions(2, AxisOptions().title("hello2"))

//     l.yAxes(0).options.title shouldEqual None
//     l.yAxes(1).options.title shouldEqual Some("hello")
//     l.yAxes(2).options.title shouldEqual Some("hello2")
//   }

}
