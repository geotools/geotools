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
 * A piecewise parametric polynomial or rational curve described
 * in terms of control points and basis functions. If the weights in the knots are equal
 * then it is a polynomial spline. If not, then it is a rational function spline. If
 * the boolean {@link #isPolynomial} is set to {@code true} then the weights shall all be set to 1.
 * A B-spline curve is a piecewise Bézier curve if it is quasi-uniform except that the
 * interior knots have multiplicity "degree" rather than having multiplicity one. In
 * this subtype the knot spacing shall be 1.0, starting at 0.0. A piecewise Bézier curve
 * that has only two knots, 0.0, and 1.0, each of multiplicity (degree+1), is equivalent
 * to a simple Bézier curve.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/geometry/coordinate/BSplineCurve.java $
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 *
 * @see GeometryFactory#createBSplineCurve
 */
@UML(identifier="GM_BSplineCurve", specification=ISO_19107)
public interface BSplineCurve extends SplineCurve {
    /**
     * The algebraic degree of the basis functions.
     */
    @UML(identifier="degree", obligation=MANDATORY, specification=ISO_19107)
    int getDegree();

    /**
     * Identifies particular types of curve which this spline is being used to
     * approximate. It is for information only, used to capture the original intention.
     * If no such approximation is intended, then the value of this attribute is {@code null}.
     */
    @UML(identifier="curveForm", obligation=OPTIONAL, specification=ISO_19107)
    SplineCurveForm getCurveForm();

    /**
     * Gives the type of knot distribution used in defining this spline.
     * This is for information only and is set according to the different construction-functions.
     */
    @UML(identifier="knotSpec", obligation=OPTIONAL, specification=ISO_19107)
    KnotType getKnotSpec();

    /**
     * {@code true} if this is a polynomial spline.
     */
    @UML(identifier="isPolynomial", obligation=MANDATORY, specification=ISO_19107)
    boolean isPolynomial();
}
