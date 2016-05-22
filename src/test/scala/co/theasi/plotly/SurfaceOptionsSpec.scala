package co.theasi.plotly

import org.scalatest._

class SurfaceOptionsSpec extends FlatSpec with Matchers {

  "SurfaceOptions" should "support setting opacity" in {
    val opts0 = SurfaceOptions().opacity(0.4)
    opts0.opacity shouldEqual(Some(0.4))
  }

}
