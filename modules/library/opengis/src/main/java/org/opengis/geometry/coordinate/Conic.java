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

import org.opengis.geometry.Geometry;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Any general conic curve. Any of the conic section curves can be canonically represented
 * in polar co-ordinates (&rho;, &phi;) as:
 *
 * <P><center><img src="doc-files/ConicCurve.png"></center></P>
 *
 * <P>where "<var>P</var>" is semi-latus rectum and "<var>e</var>" is the eccentricity. This gives
 * a conic with focus at the pole (origin), and the vertex on the conic nearest this focus in the
 * direction of the polar axis, <var>&phi;</var>=0.
 *
 * For <var>e</var>=0, this is a circle.
 * For 0&nbsp;&lt;&nbsp;<var>e</var>&nbsp;&lt;&nbsp;1, this is an ellipse.
 * For <var>e</var>=1, this is a parabola.
 * For <var>e</var>&gt;1, this is one branch of a hyperbola.</P>
 *
 *
 * <P>These generic conics can be viewed in a two-dimensional Cartesian parameter space
 * (<var>u</var>,&nbsp;<var>v</var>) given by the usual coordinate conversions
 * <var>u</var>=<var>&rho;</var>cos(<var>&phi;</var>) and
 * <var>v</var>=<var>&rho;</var>sin(<var>&phi;</var>).
 * We can then convert this to a 3D coordinate reference system by using an affine transformation,
 * (<var>u</var>,&nbsp;<var>v</var>) &rarr; (<var>x</var>,&nbsp;<var>y</var>,&nbsp;<var>z</var>)
 * which is defined by:</P>
 *
 * <P><center>(TODO: paste the matrix here, same as AffinePlacement)</center></P>
 *
 * <P>This gives us <var>&phi;</var> as the constructive parameter.
 * The {@linkplain DirectPosition direct position} given by
 * (<var>x</var><sub>0</sub>, <var>y</var><sub>0</sub>, <var>z</var><sub>0</sub>)
 * is the image of the origin in the local coordinate space (<var>u</var>, <var>v</var>)
 * Alternatively, the origin may be shifted to the vertex of the conic as</P>
 *
 * <P><var>u'</var> = <var>&rho;</var>cos(<var>&phi;</var>) - P/(1 + <var>e</var>)
 * &nbsp;&nbsp;and&nbsp;&nbsp;
 * <var>v'</var> = <var>&rho;</var>sin(<var>&phi;</var>)</P>
 *
 * <P>and <var>v</var> can be used as the constructive parameter.
 * In general, conics with small eccentricity and small <var>P</var>, use the first or
 * "central" representation. Those with large eccentricity or large <var>P</var> tend
 * to use the second or "linear" representation.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier="GM_Conic", specification=ISO_19107)
public interface Conic extends CurveSegment {
    /**
     * Returns an affine transformation object that maps the conic from parameter space into the
     * coordinate space of the target coordinate reference system of the conic corresponding to
     * the coordinate reference system of the {@linkplain Geometry}. This affine transformation
     * is given by the formulae in the class description.
     */
    @UML(identifier="position", obligation=MANDATORY, specification=ISO_19107)
    AffinePlacement getPosition();

    /**
     * Returns {@code false} if the affine transformation is used on the unshifted
     * (<var>u</var>, <var>v</var>) and {@code true} if the affine transformation
     * is applied to the shifted parameters (<var>u'</var>, <var>v'</var>). This controls
     * whether the focus or the vertex of the conic is at the origin in parameter space.
     */
    @UML(identifier="shifted", obligation=MANDATORY, specification=ISO_19107)
    boolean isShifted();

    /**
     * Returns the value of the eccentricity parameter "<var>e</var>" used in the defining
     * equation above. It controls the general shape of the curve, determining whether the
     * curve is a circle, ellipse, parabola, or hyperbola.
     */
    @UML(identifier="eccentricity", obligation=MANDATORY, specification=ISO_19107)
    double getEccentricity();

    /**
     * Returns the value of the parameter "<var>P</var>" used in the defining equation
     * above. It controls how broad the conic is at each of its foci.
     */
    @UML(identifier="semiLatusRectum", obligation=MANDATORY, specification=ISO_19107)
    double getSemiLatusRectum();

    /**
     * Return the start point parameter used in the constructive paramerization.
     * The following relation must be hold:
     *
     * <P><code>{@linkplain #forConstructiveParam forConstructiveParam}(getStartConstructiveParam())
     * .{@linkplain Object#equals equals}( {@link #getStartPoint getStartPoint}() )</P>
     *
     * There is no assumption that the
     * {@linkplain #getStartConstructiveParam start constructive parameter} is less than the
     * {@linkplain #getEndConstructiveParam end constructive parameter}, but the parameterization
     * must be strictly monotonic (strictly increasing, or strictly decreasing).
     */
    @UML(identifier="startConstrParam", obligation=MANDATORY, specification=ISO_19107)
    double getStartConstructiveParam();

    /**
     * Return the end point parameter used in the constructive paramerization.
     * The following relation must be hold:
     *
     * <P><code>{@linkplain #forConstructiveParam forConstructiveParam}(getEndConstructiveParam())
     * .{@linkplain Object#equals equals}( {@link #getEndPoint getEndPoint}() )</P>
     *
     * There is no assumption that the
     * {@linkplain #getStartConstructiveParam start constructive parameter} is less than the
     * {@linkplain #getEndConstructiveParam end constructive parameter}, but the parameterization
     * must be strictly monotonic (strictly increasing, or strictly decreasing).
     */
    @UML(identifier="endConstrParam", obligation=MANDATORY, specification=ISO_19107)
    double getEndConstructiveParam();
}
