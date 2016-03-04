package co.theasi.plotly

import org.scalatest._

import scalaj.http.{ Http, HttpOptions }

class PlotWriterSpec extends FlatSpec with Matchers {

  implicit val testSession = new Session {
    val credentials = Credentials("PlotlyImageTest", "786r5mecv0")
    val plotlyUrl = "https://plot.ly/clientresp"
  }

  // dummy data
  val defaultX = Vector(1.0, 2.0, 3.0)
  val defaultY = Vector(1.0, 5.0, 10.0)
  val defaultName = "hello-scala"

  "plot" should "send a plot request to Plotly" in {
    val response = plot(
      defaultX, defaultY, defaultName)(testSession)
  }

  it should "raise an exception if the authentication is wrong" in {
    val badSession = new Session {
      val credentials = Credentials("PlotlyImageTest", "not-a-key")
      val plotlyUrl = "https://plot.ly/clientresp"
    }
    intercept[PlotlyException]  {
      plot(
        defaultX, defaultY, defaultName)(badSession)
    }
  }

  it should "respond with a valid URL" in {
    val response = plot(defaultX, defaultY, defaultName)(testSession)

    // Try and hit the url returned in the response
    val request = Http(response.url)
      .option(HttpOptions.followRedirects(true))
    request.asString.code shouldEqual 200
  }

}
