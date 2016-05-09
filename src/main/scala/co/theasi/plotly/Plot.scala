package co.theasi.plotly

// /** Immutable builder for creating plot instances.
//   *
//   * ==Example usage==
//   *
//   * Let's start with a simple scatter plot:
//   * {{{
//   * import co.theasi.plotly._
//   *
//   * val xs = Vector(1, 2, 3)
//   * val ys = Vector(4.5, 8.5, 21.0)
//   *
//   * val p = Plot().withScatter(xs, ys)
//   * draw(p, "my-first-plot")
//   * }}}
//   *
//   * What if we want to have several lines on our plot:
//   * {{{
//   * import co.theasi.plotly._
//   *
//   * val xs = Vector(1, 2, 3)
//   * val ys1 = xs.map { 2 * _ }
//   * val ys2 = xs.map { -2 * _ }
//   *
//   * val p = Plot()
//   *   .withScatter(xs, ys1)
//   *   .withScatter(xs, ys2)
//   *
//   * draw(p, "my-second-plot")
//   * }}}
//   *
//   * ==The immutable builder pattern==
//   *
//   * When you call the `.withScatter` method on a `Plot` object, it
//   * returns a '''new''' plot with the new data series added.
//   * `Plot` instances are immutable. Thus, the following will '''not'''
//   * work:
//   *
//   * {{{
//   * val p = Plot().withScatter(xs, ys1)
//   * p.withScatter(xs, ys2) // No: this does not modify 'p' in place!
//   *
//   * draw(p, "my-other-plot")
//   * }}}
//   *
//   * Do this instead:
//   * {{{
//   * val p = Plot().withScatter(xs, ys1)
//   * val newPlot = p.withScatter(xs, ys2)
//   *
//   * draw(newPlot, "my-other-plot")
//   * }}}
//   *
//   * Or, better yet, use chaining to avoid creating temporary variables:
//   * {{{
//   * val p = Plot()
//   *   .withScatter(xs, ys1)
//   *   .withScatter(xs, ys2)
//   *
//   * draw(p, "another-plot")
//   * }}}
//   *
//   * All methods in this class work in the same way: they return a new
//   * instance of `Plot`. This pattern is called the immutable builder
//   * pattern: it is a variant of the common
//   * [[https://en.wikipedia.org/wiki/Builder_pattern builder pattern]]
//   * adapted for immutable objects.
//   *
//   */
// case class Plot[A <: Layout[A]](
//     val series: Vector[Series] = Vector.empty,
//     val layout: Layout[A] = SingleAxisLayout()
// ) {

//   /** Set the plot layout */
//   def layout[B <: Layout[B]](newLayout: Layout[B]): Plot[B] =
//     copy(layout = newLayout)

//   /** Add a scatter or line series to this plot
//     *
//     * @usecase def withScatter[X, Y](xs: Iterable[X], ys: Iterable[Y], options: ScatterOptions): Plot[A]
//     *   @inheritdoc
//     *
//     * @example {{{
//     * val xs = Vector(1.0, 2.0, 3.0)
//     * val ys1 = xs.map { _ * 2.0 }
//     * val ys2 = xs.map { _ * (-2.0) }
//     *
//     * val p = Plot()
//     *   .withScatter(xs, ys1)
//     *   .withScatter(xs, ys2, ScatterOptions().name("series-2"))
//     * }}}
//     *
//     * @param xs The 'xs' series. This can be an iterable of any type T,
//     *   provided an instance of the typeclass 'Writable[T]' exists.
//     * @param ys The 'ys' series.
//     * @param options (optional) Options controlling which subplot
//     *   the scatter plot is plotted on, the marker style etc.
//     *
//     * @return This plot with the scatter series added.
//     */
//   def withScatter[X: Writable, Y: Writable](
//       xs: Iterable[X],
//       ys: Iterable[Y],
//       options: ScatterOptions = ScatterOptions()
//   ): Plot[A] = {
//     val xsAsPType = xs.map { implicitly[Writable[X]].toPType }
//     val ysAsPType = ys.map { implicitly[Writable[Y]].toPType }
//     copy(series = series :+ Scatter(xsAsPType, ysAsPType, options))
//   }


// /*
//   def withSurface[Z: Writable](
//     zs: Iterable[Iterable[Z]]
//   ): Plot[A] = {
//     val zsAsPType = zs.map { implicitly[Writable[Z]].toPType }
//     val surface = Surface(
//       None[Iterable[Iterable[PType]]],
//       None[Iterable[Iterable[PType]]],
//       zsAsPType,
//       SurfaceOptions()
//     )
//     copy(series = series :+ surface)
//   }
//   */

// }
