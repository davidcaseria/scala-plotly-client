package co.theasi.plotly

import org.scalatest._

class FigureOptionsSepc extends FlatSpec with Matchers {

  "FigureOptions" should "allow setting margin options" in {
    val fo = FigureOptions()
    val fo1 = fo.margins(1, 2, 3, 4)
    val fo2 = fo.topMargin(1).rightMargin(2).bottomMargin(3).leftMargin(4)
    fo1 shouldEqual fo2
  }

  it should "allow setting paper background color" in {
    val fo = FigureOptions()
    val fo1 = fo.paperBackgroundColor(10, 20, 30)
    val fo2 = fo.paperBackgroundColor(Color.rgb(10, 20, 30))
    fo1 shouldEqual fo2
    fo.paperBackgroundColor shouldEqual None
  }

  it should "allow setting plot background color" in {
    val fo = FigureOptions()
    val fo1 = fo.plotBackgroundColor(10, 20, 30)
    val fo2 = fo.plotBackgroundColor(Color.rgb(10, 20, 30))
    fo1 shouldEqual fo2
    fo.plotBackgroundColor shouldEqual None
  }
}
