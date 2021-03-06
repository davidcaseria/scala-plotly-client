package co.theasi.plotly.writer

import org.scalatest._

import org.json4s._

import co.theasi.plotly.SurfaceOptions

class SeriesWriterSpec extends FlatSpec with Matchers {

  "toJson" should "serialize the srcs and scenes for surfaces" in {
    val srcs = List("src1")
    val sceneIndex = 2
    val options = SurfaceOptions()
    val writeInfo = SurfaceZWriteInfo(srcs, sceneIndex, options)

    val jobj = SeriesWriter.toJson(writeInfo)
    (jobj \ "zsrc") shouldEqual JString("src1")
    (jobj \ "scene") shouldEqual JString("scene2")
    (jobj \ "type") shouldEqual JString("surface")
  }

  it should "serialize the srcs and scenes for xyz-surfaces" in {
    val srcs = List("x-src", "y-src", "z-src")
    val sceneIndex = 3
    val options = SurfaceOptions()
    val writeInfo = SurfaceXYZWriteInfo(srcs, sceneIndex, options)

    val jobj = SeriesWriter.toJson(writeInfo)
    (jobj \ "xsrc") shouldEqual JString("x-src")
    (jobj \ "ysrc") shouldEqual JString("y-src")
    (jobj \ "zsrc") shouldEqual JString("z-src")
    (jobj \ "scene") shouldEqual JString("scene3")
    (jobj \ "type") shouldEqual JString("surface")
  }

}
