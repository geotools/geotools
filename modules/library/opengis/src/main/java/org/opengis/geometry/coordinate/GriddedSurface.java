/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.coordinate;

import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A {@linkplain ParametricCurveSurface parametric curve surface} defined from a rectangular grid
 * in the parameter space. The rows from this grid are control points for horizontal surface curves;
 * the columns are control points for vertical surface curves. The working assumption is that for a
 * pair of parametric coordinates (<var>s</var>,&nbsp;<var>t</var>), that the horizontal curves for
 * each integer offset are calculated and evaluated at <var>s</var>. This defines a sequence of
 * control points:
 *
 * <blockquote>
 * &lt;c<sub>n</sub>(<var>s</var>) : <var>s</var> = 1 &hellip; columns&gt;
 * </blockquote>
 *
 * From this sequence, a vertical curve is calculated for <var>s</var>, and evaluated at <var>t</var>.
 * In most cases, the order of calculation (horizontal-vertical versus vertical-horizontal) does not
 * make a difference. Where it does, the horizontal-vertical order shall be the one used.
 * <p>
 * The most common case of a gridded surface is a 2D spline. In this case the weight functions for
 * each parameter make order of calculation unimportant:
 *
 * <blockquote>TODO: copy equations there</blockquote>
 *
 * Logically, any pair of curve interpolation types can lead to a subtype of {@code GriddedSurface}.
 * The sub-interfaces provided in this package define some of the most commonly encountered surfaces
 * that can be represented in this manner.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
@UML(identifier="GM_GriddedSurface", specification=ISO_19107)
public interface GriddedSurface extends ParametricCurveSurface {
    /**
     * Returns the doubly indexed sequence of control points, given in row major form.
     * There is no assumption made about the shape of the grid. For example, the positions
     * need not effect a "2&frac12;D" surface, consecutive points may be equal in any or
     * all of their ordinates. Further, the curves in either or both directions may close.
     */
    @UML(identifier="controlPoint", obligation=MANDATORY, specification=ISO_19107)
    PointGrid getControlPoints();

    /**
     * Returns the number of rows in the parameter grid.
     */
    @UML(identifier="rows", obligation=MANDATORY, specification=ISO_19107)
    int getRows();

    /**
     * Returns the number of columns in the parameter grid.
     */
    @UML(identifier="columns", obligation=MANDATORY, specification=ISO_19107)
    int getColumns();
}
