// package co.theasi.plotly

// import org.scalatest._

// class LayoutSpec extends FlatSpec with Matchers with Inside {

//   def size(ax: Axis) = ax.domain._2 - ax.domain._1

//   "GridLayout" should "create subplots in a row" in {
//     val l = GridLayout(1, 2)
//     val hspace = GridLayout.DefaultHorizontalSpacing / 2.0
//     val vspace = GridLayout.DefaultVerticalSpacing / 1.0
//     l.xAxes.size shouldEqual 2
//     l.yAxes.size shouldEqual 2
//     l.xAxes(0).domain shouldEqual((0.0, 0.5 - 0.5*hspace))
//     l.xAxes(1).domain shouldEqual((0.5 + 0.5*hspace, 1.0))
//     //l.yAxes.map { ax => ax.domain shouldEqual((0.0, 1.0)) }
//   }

//   it should "create subplots in a column" in {
//     val l = GridLayout(2, 1)
//     val hspace = GridLayout.DefaultHorizontalSpacing / 1.0
//     val vspace = GridLayout.DefaultVerticalSpacing / 2.0
//     l.xAxes.size shouldEqual 2
//     l.yAxes.size shouldEqual 2
//     l.xAxes.map { ax => ax.domain shouldEqual((0.0, 1.0)) }
//     l.yAxes(0).domain shouldEqual((0.5 + 0.5*vspace, 1.0))

//     // FPE occurs for yAxes(1) -> compare using approximate equality
//     inside(l.yAxes(1).domain) { case (bottom, top) =>
//       bottom shouldEqual 0.0 +- 1e-5
//       top shouldEqual(0.5 - 0.5*vspace) +- 1e-5
//     }
//   }

//   it should "create subplots in a grid" in {
//     val l = GridLayout(4, 5) // 4 rows, 5 columns
//     val hspace = GridLayout.DefaultHorizontalSpacing / 4.0
//     val vspace = GridLayout.DefaultVerticalSpacing / 5.0

//     l.xAxes.size shouldEqual 20
//     l.yAxes.size shouldEqual 20

//     // Check xAxes span each row
//     (0 until 4).foreach { irow =>
//       l.xAxis(irow, 0).domain._1 shouldEqual 0.0
//       l.xAxis(irow, 4).domain._2 shouldEqual 1.0
//     }

//     // Check yAxes span each column
//     (0 until 5).foreach { icol =>
//       l.yAxis(0, icol).domain._2 shouldEqual 1.0 +- 1e-5
//       l.yAxis(3, icol).domain._1 shouldEqual 0.0 +- 1e-5
//     }

//     // Check xAxes all have same width
//     val width = size(l.xAxes(0))
//     l.xAxes.foreach { ax => size(ax) shouldEqual width +- 1e-5 }

//     val height = size(l.yAxes(0))
//     l.yAxes.foreach { ax => size(ax) shouldEqual height +- 1e-5 }

//   }

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

//   "Layout" should "allow setting margin options" in {
//     val l = SingleAxisLayout()
//     val l1 = l.margins(1, 2, 3, 4)
//     val l2 = l.topMargin(1).rightMargin(2).bottomMargin(3).leftMargin(4)
//     l1 shouldEqual l2
//   }

//   it should "allow setting paper background color" in {
//     val l = SingleAxisLayout()
//     val l1 = l.paperBackgroundColor(10, 20, 30)
//     val l2 = l.paperBackgroundColor(Color.rgb(10, 20, 30))
//     l1 shouldEqual l2
//     l.options.paperBackgroundColor shouldEqual None
//   }

//   it should "allow setting plot background color" in {
//     val l = SingleAxisLayout()
//     val l1 = l.plotBackgroundColor(10, 20, 30)
//     val l2 = l.plotBackgroundColor(Color.rgb(10, 20, 30))
//     l1 shouldEqual l2
//     l.options.plotBackgroundColor shouldEqual None
//   }

// }
