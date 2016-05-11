package co.theasi.plotly

case class FigureOptions(
  title: Option[String],
  legendOptions: LegendOptions,
  margins: Margins,
  width: Option[Int],
  height: Option[Int],
  paperBackgroundColor: Option[Color],
  plotBackgroundColor: Option[Color]
) {

  /** Set figure title */
  def title(newTitle: String): FigureOptions =
    copy(title = Some(newTitle))

  /** Set legend options
    *
    * {{{
    * val legend = LegendOptions().fontSize(20)
    * val layout = SingleAxisLayout().legendOptions(legend)
    * }}}
    */
  def legend(newLegend: LegendOptions): FigureOptions =
    copy(legendOptions = newLegend)

  private def margins(newMargins: Margins): FigureOptions =
    copy(margins = newMargins)

  /** Set layout margins */
  def margins(top: Int, right: Int, bottom: Int, left: Int): FigureOptions =
    margins(Margins(Some(top), Some(right), Some(bottom), Some(left)))

  /** Set left margin (in px) */
  def leftMargin(newLeftMargin: Int): FigureOptions = {
    val newMargin = margins.copy(left = Some(newLeftMargin))
    margins(newMargin)
  }
  /** Set right margin (in px) */
  def rightMargin(newRightMargin: Int): FigureOptions = {
    val newMargin = margins.copy(right = Some(newRightMargin))
    margins(newMargin)
  }
  /** Set top margin (in px) */
  def topMargin(newTopMargin: Int): FigureOptions = {
    val newMargin = margins.copy(top = Some(newTopMargin))
    margins(newMargin)
  }
  /** Set bottom margin (in px) */
  def bottomMargin(newBottomMargin: Int): FigureOptions = {
    val newMargin = margins.copy(bottom = Some(newBottomMargin))
    margins(newMargin)
  }

  /** Set the plot width (in px) */
  def width(newWidth: Int): FigureOptions = copy(width = Some(newWidth))

  /** Set the plot height (in px) */
  def height(newHeight: Int): FigureOptions =
    copy(height = Some(newHeight))

  /** Set the paper's background color
    *
    * {{{
    * val layout = SingleAxisLayout().paperBackgroundColor(Color.rgb(0, 255, 0))
    * }}}
    */
  def paperBackgroundColor(newColor: Color): FigureOptions =
    copy(paperBackgroundColor = Some(newColor))
  /** Set the paper's background color with (red, green, blue, alpha) */
  def paperBackgroundColor(r: Int, g: Int, b: Int, a: Double): FigureOptions =
    paperBackgroundColor(Color.rgba(r, g, b, a))
  /** Set the paper's background color. */
  def paperBackgroundColor(r: Int, g: Int, b: Int): FigureOptions =
    paperBackgroundColor(Color.rgb(r, g, b))

  /** Set the plot's background color
    *
    * {{{
    * val layout = SingleAxisLayout().plotBackgroundColor(Color.rgb(0, 255, 0))
    * }}}
    */
  def plotBackgroundColor(newColor: Color): FigureOptions =
    copy(plotBackgroundColor = Some(newColor))
  /** Set the plot's background color with (red, green, blue, alpha) */
  def plotBackgroundColor(r: Int, g: Int, b: Int, a: Double): FigureOptions =
    plotBackgroundColor(Color.rgba(r, g, b, a))
  /** Set the plot's background color */
  def plotBackgroundColor(r: Int, g: Int, b: Int): FigureOptions =
    plotBackgroundColor(Color.rgb(r, g, b))

}

object FigureOptions {
  def apply(): FigureOptions = FigureOptions(
    title = None,
    legendOptions = LegendOptions(),
    margins = Margins(),
    width = None,
    height = None,
    paperBackgroundColor = None,
    plotBackgroundColor = None
  )
}
