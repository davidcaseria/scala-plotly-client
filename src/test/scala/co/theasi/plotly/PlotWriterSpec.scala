package co.theasi.plotly

import org.scalatest._

import scalaj.http.{ Http, HttpOptions }

class PlotWriterSpec extends FlatSpec with Matchers {

  val writer = new Writer {
    val credentials = Credentials("PlotlyImageTest", "786r5mecv0")
    val plotlyUrl = "https://plot.ly/"
  }

  // dummy data
  val defaultX = Vector(1.0, 2.0, 3.0)
  val defaultY = Vector(1.0, 5.0, 10.0)
  val defaultName = "hello-scala"

  "plot" should "send a plot request to Plotly" in {
    val response = writer.plot(
      defaultX, defaultY, defaultName)
  }

  it should "raise an exception if the authentication is wrong" in {
    val badWriter = new Writer {
      val credentials = Credentials("PlotlyImageTest", "not-a-key")
      val plotlyUrl = "https://plot.ly/clientresp"
    }
    intercept[PlotlyException]  {
      badWriter.plot(
        defaultX, defaultY, defaultName)
    }
  }

  it should "respond with a valid URL" in {
    val response = writer.plot(defaultX, defaultY, defaultName)

    // Try and hit the url returned in the response
    val request = Http(response.url)
      .option(HttpOptions.followRedirects(true))
    request.asString.code shouldEqual 200
  }

  it should "work with integer x-values" in {
    val x = defaultX.map { _.toInt }
    val response = writer.plot(x, defaultY, defaultName)

    // Try and hit the url returned in the response
    val request = Http(response.url)
      .option(HttpOptions.followRedirects(true))
    request.asString.code shouldEqual 200
  }

}
