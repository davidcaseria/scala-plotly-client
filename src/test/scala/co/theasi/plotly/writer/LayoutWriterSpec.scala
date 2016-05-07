// package co.theasi.plotly.writer

// import org.json4s._
// import org.json4s.native.JsonMethods._

// import org.scalatest._

// import co.theasi.plotly._

// class LayoutWriterSpec extends FlatSpec with Matchers {
//   "Layout.toJson" should "serialize axis domains" in {
//     val layout = GridLayout(1, 2)
//     val x0Domain = layout.xAxes(0).domain
//     val x1Domain = layout.xAxes(1).domain
//     val jobj = LayoutWriter.toJson(layout)
//     (jobj \ "xaxis" \ "domain")(0) shouldEqual
//       JDouble(x0Domain._1)
//     (jobj \ "xaxis" \ "domain")(1) shouldEqual
//       JDouble(x0Domain._2)
//     (jobj \ "xaxis2" \ "domain")(0) shouldEqual
//       JDouble(x1Domain._1)
//     (jobj \ "xaxis2" \ "domain")(1) shouldEqual
//       JDouble(x1Domain._2)
//     List("yaxis", "yaxis2").foreach { axis =>
//       (jobj \ axis \ "domain")(0) shouldEqual
//         JDouble(0.0)
//       (jobj \ axis \ "domain")(1) shouldEqual
//         JDouble(1.0)
//     }
//   }

//   it should "serialize axis anchors" in {
//     val layout = GridLayout(1, 2)
//     val jobj = LayoutWriter.toJson(layout)
//     (jobj \ "xaxis" \ "anchor") shouldEqual JString("y")
//     (jobj \ "xaxis2" \ "anchor") shouldEqual JString("y2")
//     (jobj \ "yaxis" \ "anchor") shouldEqual JString("x")
//     (jobj \ "yaxis2" \ "anchor") shouldEqual JString("x2")
//   }

//   it should "serialize axis titles" in {
//     val layout = SingleAxisLayout().xAxisOptions(AxisOptions().title("hello"))
//     val jobj = LayoutWriter.toJson(layout)
//     (jobj \ "xaxis" \ "title") shouldEqual JString("hello")
//     (jobj \ "yaxis" \ "title") shouldEqual JNothing
//   }

//   it should "serialize legend options" in {
//     val layout = SingleAxisLayout().legend(LegendOptions()
//       .x(0.5)
//       .y(0.6)
//       .xAnchor(XAnchor.Left)
//       .yAnchor(YAnchor.Top))
//     val jobj = LayoutWriter.toJson(layout) \ "legend"
//     jobj \ "x" shouldEqual JDouble(0.5)
//     jobj \ "y" shouldEqual JDouble(0.6)
//     jobj \ "xanchor" shouldEqual JString("left")
//     jobj \ "yanchor" shouldEqual JString("top")
//   }

//   it should "not include 'legend' if the LegendOptions are empty" in {
//     val layout = SingleAxisLayout()
//     val jobj = LayoutWriter.toJson(layout) \ "legend"
//     jobj shouldEqual JNothing
//   }

//   it should "serialize the margin options" in {
//     val layout = SingleAxisLayout().leftMargin(10).rightMargin(20)
//     val jobj = LayoutWriter.toJson(layout) \ "margin"
//     jobj \ "l" shouldEqual JInt(10)
//     jobj \ "r" shouldEqual JInt(20)
//     jobj \ "t" shouldEqual JNothing
//     jobj \ "b" shouldEqual JNothing
//   }

//   it should "not include 'margin' if the Margins are empty" in {
//     val layout = SingleAxisLayout()
//     val jobj = LayoutWriter.toJson(layout) \ "margin"
//     jobj shouldEqual JNothing
//   }

//   it should "include width and height if set" in {
//     val layout = SingleAxisLayout()
//     (LayoutWriter.toJson(layout.width(10)) \ "width") shouldEqual JInt(10)
//     (LayoutWriter.toJson(layout.height(20)) \ "height") shouldEqual JInt(20)
//     val jobj = LayoutWriter.toJson(layout)
//     jobj \ "width" shouldEqual JNothing
//     jobj \ "height" shouldEqual JNothing
//   }

//   it should "include the paper and plot background color if set" in {
//     val c1 = Color.rgb(10, 20, 30)
//     val c2 = Color.rgb(100, 200, 250)
//     val layout = SingleAxisLayout()
//     ((LayoutWriter.toJson(layout.plotBackgroundColor(c1)) \ "plot_bgcolor")
//       shouldEqual JString(ColorWriter.toJson(c1)))
//     ((LayoutWriter.toJson(layout.paperBackgroundColor(c2)) \ "paper_bgcolor")
//       shouldEqual JString(ColorWriter.toJson(c2)))
//     (LayoutWriter.toJson(layout) \ "plot_bgcolor") shouldEqual JNothing
//     (LayoutWriter.toJson(layout) \ "paper_bgcolor") shouldEqual JNothing
//   }
// }
