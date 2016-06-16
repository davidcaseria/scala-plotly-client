package co.theasi.plotly

trait Plot {
  type OptionType
  def series: Vector[Series]
  val options: OptionType
}

object Plot {
  def apply(): CartesianPlot = CartesianPlot()
}


/** Plot with Cartesian axes
  *
  * ==Example usage==
  *
  * This class draws a sub-plot on Cartesian axes. This can be used
  * to draw bar charts, scatter plots, box plots, contour plots etc.:
  * anything that has an x- and a y-axis.
  *
  * `CartesianPlot` instances can be sent directly to Plotly:
  * {{{
  * val xs = Vector(1, 2, 5)
  * val ys = Vector(2, 4, 9)
  * val plot = CartesianPlot().withScatter(xs, ys)
  * draw(plot, "my-scatter-plot")
  * }}}
  *
  * They can also be embedded in [[Figure]] instances for more complex
  * plot layouts:
  * {{{
  * val xs = Vector(1, 2, 5)
  * val ys1 = Vector(2, 4, 9)
  * val ys2 = Vector(2, 6, 12)
  *
  * val figure = RowFigure(2) // 2 sub-plots next to each other
  *   .plot(0) { CartesianPlot().withScatter(xs, ys1) } // left subplot
  *   .plot(1) { CartesianPlot().withScatter(xs, ys2) } // right subplot
  *
  * draw(figure, "multiple-plots")
  * }}}
  *
  * ==Multiple lines==
  *
  * `CartesianPlot` instances support multiple series:
  * {{{
  * val xs = Vector(1, 2, 3)
  * val ys1 = xs.map { 2 * _ }
  * val ys2 = xs.map { -2 * _ }
  *
  * val p = CartesianPlot()
  *   .withScatter(xs, ys1, ScatterOptions().name("series-1"))
  *   .withScatter(xs, ys2, ScatterOptions().name("series-2"))
  *
  * draw(p, "multiple-series")
  * }}}
  *
  *
  * ==The immutable builder pattern==
  *
  * When you call the `.withScatter` method on a `CartesianPlot` object, it
  * returns a '''new''' plot with the new data series added.
  * `CartesianPlot` instances are immutable. Thus, the following will '''not'''
  * work:
  *
  * {{{
  * // Not what you want!
  * val p = CartesianPlot().withScatter(xs, ys1)
  * p.withScatter(xs, ys2) // No: this does not modify 'p' in place!
  *
  * draw(p, "my-other-plot")
  * }}}
  *
  * Do this instead:
  * {{{
  * val p = CartesianPlot().withScatter(xs, ys1)
  * val newPlot = p.withScatter(xs, ys2)
  *
  * draw(newPlot, "my-other-plot")
  * }}}
  *
  * Or, better yet, use chaining to avoid creating temporary variables:
  * {{{
  * val p = CartesianPlot()
  *   .withScatter(xs, ys1)
  *   .withScatter(xs, ys2)
  *
  * draw(p, "another-plot")
  * }}}
  *
  * All methods in this class work in the same way: they return a new
  * instance of `CartesianPlot`. This pattern is called the immutable builder
  * pattern: it is a variant of the common
  * [[https://en.wikipedia.org/wiki/Builder_pattern builder pattern]]
  * adapted for immutable objects.
  */
case class CartesianPlot(
    series: Vector[CartesianSeries],
    options: CartesianPlotOptions)
extends Plot {
  type OptionType = CartesianPlotOptions

  /** Add a scatter plot to this plot.
    * 
    * @usecase def withScatter[X, Y](xs: Iterable[X], ys: Iterable[Y], options: ScatterOptions): CartesianPlot
    *   @inheritdoc
    *
    * @example {{{
    * val xs = Vector(1.0, 2.0, 3.0)
    * val ys1 = xs.map { _ * 2.0 }
    * val ys2 = xs.map { _ * (-2.0) }
    *
    * val p = CartesianPlot()
    *   .withScatter(xs, ys1)
    *   .withScatter(xs, ys2, ScatterOptions().name("series-2"))
    * }}}
    *
    * @param xs The 'xs' series. This can be an iterable of any type T,
    *   provided an instance of the typeclass 'Writable[T]' exists.
    * @param ys The 'ys' series.
    * @param options (optional) Options controlling the plot style.
    *
    * @return Copy of this plot with the scatter series added.
    */
  def withScatter[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y],
      options: ScatterOptions = ScatterOptions()
  ): CartesianPlot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = series :+ Scatter(xsAsPType, ysAsPType, options))
  }

  /** Add a bar series to this plot. */
  def withBar[X: Writable, Y: Writable](
      xs: Iterable[X],
      ys: Iterable[Y],
      options: BarOptions = BarOptions()
  ): CartesianPlot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
    copy(series = series :+ Bar(xsAsPType, ysAsPType, options))
  }

  /** Add a box plot to this plot. */
  def withBox[X: Writable](
    xs: Iterable[X]
  ): CartesianPlot = {
    val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
    copy(series = series :+ Box(xsAsPType, BoxOptions()))
  }

  /** Set the x-axis options for this plot. */
  def xAxisOptions(newAxisOptions: AxisOptions): CartesianPlot = {
    val newAxis = options.xAxis.copy(options = newAxisOptions)
    val newOptions = options.copy(xAxis = newAxis)
    copy(options = newOptions)
  }

  /** Set the y-axis options for this plot. */
  def yAxisOptions(newAxisOptions: AxisOptions): CartesianPlot = {
    val newAxis = options.yAxis.copy(options = newAxisOptions)
    val newOptions = options.copy(yAxis = newAxis)
    copy(options = newOptions)
  }

}


object CartesianPlot {

  def apply(): CartesianPlot = CartesianPlot(
    Vector.empty[CartesianSeries], CartesianPlotOptions())

}


/** Plot with 3D axes
  *
  *  ==Example usage==
  *
  * This class represents plots with 3D Cartesian axes. This can
  * be used to draw 3D surface plots.
  *
  * `ThreeDPlot` instances can be sent directly to Plotly:
  * {{{
  * val zs = Vector(
  *   Vector(1.0, 2.0, 1.0),
  *   Vector(5.0, 4.0, 5.0),
  *   Vector(1.0, 2.0, 1.0)
  * )
  *
  * val plot = ThreeDPlot().withSurface(zs)
  * draw(plot, "my-3d-plot")
  * }}}
  *
  * They can also be embedded in [[Figure]] instances for
  * more complex plot layouts:
  * {{{
  * val zs1 = Vector(
  *   Vector(1.0, 2.0, 1.0),
  *   Vector(5.0, 4.0, 5.0),
  *   Vector(1.0, 2.0, 1.0)
  * )
  *
  * val zs2 = zs1.map { row => row.map { _ - 2.0 } }
  *
  * val figure = RowFigure(2) // 2 sub-plots next to each other
  *   .plot(0) { ThreeDPlot().withSurface(zs1) } // left sub-plot
  *   .plot(1) { ThreeDPlot().withSurface(zs2) } // right sub-plot
  *
  * draw(figure, "3d-subplots")
  * }}}
  *
  * ==Multiple surfaces==
  *
  * `ThreeDPlot` instances support multiple surfaces:
  * {{{
  * val zs1 = Vector(
  *   Vector(1.0, 2.0, 1.0),
  *   Vector(5.0, 4.0, 5.0),
  *   Vector(1.0, 2.0, 1.0)
  * )
  *
  * val zs2 = zs1.map { row => row.map { _ - 2.0 } }
  *
  * val p = ThreeDPlot()
  *   .withSurface(zs1, SurfaceOptions().name("top"))
  *   .withSurface(zs2, SurfaceOptions().name("bottom"))
  * }}}
  *
  * ==The immutable builder pattern==
  *
  * When you call the `.withSurface` method on a `ThreeDPlot` object, it
  * returns a '''new''' plot with the series added. `ThreeDPlot` instances
  * are immutable. Thus, the following does '''not''' work as expected:
  *
  * {{{
  * // Not what you want!
  * val plot = ThreeDPlot().withSurface(zs)
  * plot.withSurface(zs2) // No: this does not modify 'plot' in place!
  * }}}
  *
  * You should chain calls to `withSurface` instead:
  * {{{
  * val plot = ThreeDPlot()
  *   .withSurface(zs1)
  *   .withSurface(zs2)
  *  }}}
  * 
  * All methods in this class work in the same way: they return a new
  * instance of `ThreeDPlot`. This pattern is called the immutable builder
  * pattern: it is a variant of the common
  * [[https://en.wikipedia.org/wiki/Builder_pattern builder pattern]]
  * adapted for immutable objects.
  *
  * @define axisOptionsExample @example {{{
  * val zs = Vector.tabulate(3, 4) { (i, j) => util.Random.nextDouble }
  *
  * val p = ThreeDPlot()
  *   .withSurface(zs)
  *   .xAxisOptions(AxisOptions().title("x-axis").noZeroLine)
  *   .yAxisOptions(AxisOptions().title("y-axis").titleColor(255, 0, 0))
  *   .zAxisOptions(AxisOptions().title("z-axis").noGrid)
  * }}}
  * 
  */
case class ThreeDPlot(
  series: Vector[Series],
  options: ThreeDPlotOptions)
extends Plot {

  type OptionType = ThreeDPlotOptions

  /** Add a surface plot to this plot.
    *
    * @usecase def withSurface[Z](zs: Iterable[Iterable[Z]], options: SurfaceOptions): ThreeDPlot
    *   @inheritdoc
    *
    * @example {{{
    * val zs = Vector(
    *   Vector(1.0, 2.0, 1.0),
    *   Vector(5.0, 4.0, 5.0),
    *   Vector(3.0, 2.0, 3.0),
    *   Vector(1.0, 2.0, 1.0)
    * )
    *
    * val p = ThreeDPlot()
    *   .withSurface(zs, SurfaceOptions().name("series-1"))
    * }}}
    *
    * @param zs The values of z. This is an iterable of iterables of any
    *   type T, provided an instance of the typeclass 'Writable[T]' exists.
    *   The values of `zs` are oriented such that `zs(0)(1)` corresponds to
    *   the value of z at x = 0 and y = 1.
    * @param options (optional) Options controlling the style in which the
    *   surface is drawn.
    *
    * @return Copy of this plot with the surface series added.
    */
  def withSurface[Z: Writable](
    zs: Iterable[Iterable[Z]],
    options: SurfaceOptions = SurfaceOptions()
  ): ThreeDPlot = {
    val zsAsPType = zs.map { zRow =>
      zRow.map { implicitly[Writable[Z]].toPType }
    }
    copy(series = series :+ SurfaceZ(zsAsPType, options))

  }

  /** Set options for the x-axis
    *
    * $axisOptionsExample
    *
    * @param newAxisOptions The new option values.
    *
    * @return Copy of this plot with the new axis options set.
    */
  def xAxisOptions(newAxisOptions: AxisOptions): ThreeDPlot = {
    val newOptions = options.copy(xAxisOptions = newAxisOptions)
    copy(options = newOptions)
  }

  /** Set options for the y-axis
    *
    * $axisOptionsExample
    *
    * @param newAxisOptions The new option values.
    *
    * @return Copy of this plot with the new axis options set.
    */
  def yAxisOptions(newAxisOptions: AxisOptions): ThreeDPlot = {
    val newOptions = options.copy(yAxisOptions = newAxisOptions)
    copy(options = newOptions)
  }

  /** Set options for the z-axis
    *
    * $axisOptionsExample
    *
    * @param newAxisOptions The new option values.
    *
    * @return Copy of this plot with the new axis options set.
    */
  def zAxisOptions(newAxisOptions: AxisOptions): ThreeDPlot = {
    val newOptions = options.copy(zAxisOptions = newAxisOptions)
    copy(options = newOptions)
  }

}


object ThreeDPlot {
  def apply(): ThreeDPlot = ThreeDPlot(
    Vector.empty[Series], ThreeDPlotOptions())
}
