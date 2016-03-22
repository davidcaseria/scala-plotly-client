package co.theasi.plotly.writer

import org.json4s._
import org.json4s.native.JsonMethods._

import org.scalatest._

import co.theasi.plotly._

class PlotWriterSpec extends FlatSpec with Matchers {

  /*implicit val testServer = new Server {
    override val credentials = Credentials("PlotlyImageTest", "786r5mecv0")
    override val url = "https://api.plot.ly/v2"
  }*/
  val testX1 = Vector(1.0, 2.0, 3.0)
  val testX2 = Vector(1, 2, 3)
  val testY1 = Vector(4.0, 5.0, 7.0)

  def checkTestX1(arr: JValue) = {
    val JArray(response) = arr
    response.toVector shouldEqual testX1.map { JDouble }
  }

  def checkTestX2(arr: JValue) = {
    val JArray(response) = arr
    response.toVector shouldEqual testX2.map { JInt(_) }
  }

  def checkTestY1(arr: JValue) = {
    val JArray(response) = arr
    response.toVector shouldEqual testY1.map { JDouble }
  }

  def getJsonForPlotFile(plotFile: PlotFile): JValue = {
    val endPoint = s"plots/${plotFile.fileId}/content"
    val req = Api.get(endPoint, Seq("inline_data" -> "true"))
    Api.despatchAndInterpret(req)
  }

  "draw" should "draw a basic scatter plot" in {
    val p = Plot().withScatter(testX1, testY1)
    val plotFile = PlotWriter.draw(p, "test-123")

    // Verify that the plot is correct
    val jsonResponse = getJsonForPlotFile(plotFile)
    val series = (jsonResponse \ "data")(0)
    checkTestX1(series \ "x")
    checkTestY1(series \ "y")
  }

  it should "draw a scatter plot with mixed Int/Double" in {
    val p = Plot().withScatter(testX2, testY1)
    val plotFile = PlotWriter.draw(p, "test-124")

    val jsonResponse = getJsonForPlotFile(plotFile)
    val series = (jsonResponse \ "data")(0)
    checkTestX2(series \ "x")
    checkTestY1(series \ "y")
  }

  it should "draw a scatter plot with set mode" in {
    val options0 = ScatterOptions(mode = Set(ScatterMode.Line))
    val options1 = ScatterOptions(mode = Set(ScatterMode.Marker, ScatterMode.Line))
    val p = Plot()
      .withScatter(testX1, testY1, options0)
      .withScatter(testX1, testY1, options1)
    val plotFile = PlotWriter.draw(p, "test-125")

    val jsonResponse = getJsonForPlotFile(plotFile)
    val series0 = (jsonResponse \ "data")(0)
    val JString(mode0) = series0 \ "mode"
    mode0 should (equal("lines+markers") or equal("markers+lines"))
    val series1 = (jsonResponse \ "data")(1)
    val JString(mode1) = series1 \ "mode"
    mode1 shouldEqual "lines"
  }
}
