package co.theasi.plotly.writer

import org.scalatest._

import org.json4s._

import co.theasi.plotly.SurfaceOptions

class OptionsWriterSpec extends FlatSpec with Matchers {

  "surfaceOptionsToJson" should "serialize the colorscale if present" in {
    val surfaceOptions = SurfaceOptions().colorscale("Viridis")
    val jobj = OptionsWriter.surfaceOptionsToJson(surfaceOptions)
    (jobj \ "colorscale") shouldEqual JString("Viridis")
  }

  it should "omit the colorscale if absent" in {
    val surfaceOptions = SurfaceOptions()
    val jobj = OptionsWriter.surfaceOptionsToJson(surfaceOptions)
    (jobj \ "colorscale") shouldEqual JNothing
  }

}
