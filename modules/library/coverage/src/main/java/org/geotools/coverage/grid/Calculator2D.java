/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.coverage.grid;


/**
 * A grid coverage backed by the same {@linkplain #image image}, {@linkplain #gridGeometry grid
 * geometry} and {@linkplain #getSampleDimensions sample dimension} than an other coverage, but
 * performing some additional calculation in its {@code evaluate} methods.
 *
 * @since 2.5
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class Calculator2D extends GridCoverage2D {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -6287856672249003253L;

    /**
     * The source grid coverage which was specified at construction time (never {@code null}).
     *
     * @serial This field duplicate the value obtained by <code>{@linkplain #getSources()}(0)</code>
     *         except if this coverage has been deserialized. The source is required in order to get
     *         the {@link #view} method to work. Because the {@linkplain GridCoverage2D#image image}
     *         contained in the source is the same one than in this {@link Calculator2D}, there is
     *         few cost in keeping it.
     */
    protected final GridCoverage2D source;

    /**
     * Constructs a new grid coverage with the same parameter than the specified coverage.
     *
     * @param name The name for this coverage, or {@code null} for the same than {@code coverage}.
     * @param coverage The source grid coverage.
     */
    protected Calculator2D(final CharSequence name, final GridCoverage2D coverage) {
        super(name, coverage);
        this.source = coverage;
    }

    /**
     * Invoked by <code>{@linkplain #view view}(type)</code> when the {@linkplain ViewType#PACKED
     * packed}, {@linkplain ViewType#GEOPHYSICS geophysics} or {@linkplain ViewType#PHOTOGRAPHIC
     * photographic} view of this grid coverage needs to be created. The {@link #view view} method
     * first gets the desired view from the {@linkplain #source} coverage, then passes it as the
     * argument to this method. Subclasses should define this method as below:
     *
     * <blockquote><code>
     * return new MyCalculator2D(view, &lt;</code><var>any configuration to copy</var><code>&gt;);
     * </code></blockquote>
     *
     * @param  view A view derived from the {@linkplain #source} coverage.
     * @return The grid coverage to be returned by {@link #view view}, typically of the same
     *         class than {@code this} (but this is not a strong requirement).
     */
    @Override
    protected abstract GridCoverage2D specialize(GridCoverage2D view);

    /**
     * Returns the native view to be given to a newly created {@link ViewsManager}. We can not
     * returns {@code this} like what {@link GridCoverage2D} does because this class is just a
     * decorator around a {@linkplain #source}, and the later may not be a native view - it can
     * be anything like a geophysics, a packed, <cite>etc</cite>. We can hardly call any instance
     * of this decorator as "native", so the safest approach is to use the native view of the
     * source. This is needed for proper working of {@link ViewsManager}: if this decorator stands
     * between it and the views that it creates, it may not realize that a view was already created.
     */
    @Override
    final GridCoverage2D getNativeView() {
        return source.view(ViewType.NATIVE);
    }

    /**
     * Returns the class of the view returned by {@link #specialize}, or {@code null} if unknown.
     * Default implementation returns {@code null} because we don't know how the user will
     * implement {@link #specialize}. GeoTools final subclasses like {@link Interpolator2D}
     * will return their class.
     */
    @Override
    Class<? extends Calculator2D> getViewClass() {
        return null;
    }
}
