package co.theasi.plotly.writer

import org.scalatest._

import org.json4s._

import co.theasi.plotly._

class FigureOptionsWriterSpec extends FlatSpec with Matchers {

  "toJson" should "serialize legend options" in {
    val figure = SinglePlotFigure().legend(
      LegendOptions()
        .x(0.5)
        .y(0.6)
        .xAnchor(XAnchor.Left)
        .yAnchor(YAnchor.Top))

    val jobj = FigureOptionsWriter.toJson(figure.options) \ "legend"
    jobj \ "x" shouldEqual JDouble(0.5)
    jobj \ "y" shouldEqual JDouble(0.6)
    jobj \ "xanchor" shouldEqual JString("left")
    jobj \ "yanchor" shouldEqual JString("top")
  }

  it should "not include 'legend' if the LegendOptions are empty" in {
    val figure = SinglePlotFigure()
    val jobj = FigureOptionsWriter.toJson(figure.options) \ "legend"
    jobj shouldEqual JNothing
  }

  it should "serialize the margin options" in {
    val figure = SinglePlotFigure().leftMargin(10).rightMargin(20)
    val jobj = FigureOptionsWriter.toJson(figure.options) \ "margin"
    jobj \ "l" shouldEqual JInt(10)
    jobj \ "r" shouldEqual JInt(20)
    jobj \ "t" shouldEqual JNothing
    jobj \ "b" shouldEqual JNothing
  }

  it should "not include 'margin' if the Margins are empty" in {
    val figure = SinglePlotFigure()
    val jobj = FigureOptionsWriter.toJson(figure.options) \ "margin"
    jobj shouldEqual JNothing
  }

  it should "include width and height if set" in {
    val figure = SinglePlotFigure()
    val fig1 = figure.width(10)
    val fig2 = figure.height(20)
    (FigureOptionsWriter.toJson(fig1.options) \ "width") shouldEqual JInt(10)
    (FigureOptionsWriter.toJson(fig2.options) \ "height") shouldEqual JInt(20)
    val jobj = FigureOptionsWriter.toJson(figure.options)
    jobj \ "width" shouldEqual JNothing
    jobj \ "height" shouldEqual JNothing
  }

  it should "include the paper and plot background color if set" in {
    val c1 = Color.rgb(10, 20, 30)
    val c2 = Color.rgb(100, 200, 250)
    val figure = SinglePlotFigure()
    val fig1 = figure.plotBackgroundColor(c1)
    val fig2 = figure.paperBackgroundColor(c2)
    ((FigureOptionsWriter.toJson(fig1.options) \ "plot_bgcolor")
      shouldEqual JString(ColorWriter.toJson(c1)))
    ((FigureOptionsWriter.toJson(fig2.options) \ "paper_bgcolor")
      shouldEqual JString(ColorWriter.toJson(c2)))
    val jobj = FigureOptionsWriter.toJson(figure.options)
    (jobj \ "plot_bgcolor") shouldEqual JNothing
    (jobj \ "paper_bgcolor") shouldEqual JNothing
  }

}
