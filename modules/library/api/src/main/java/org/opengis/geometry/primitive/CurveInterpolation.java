/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.geometry.primitive;

import static org.opengis.annotation.Obligation.CONDITIONAL;
import static org.opengis.annotation.Specification.ISO_19107;

import java.util.ArrayList;
import java.util.List;
import org.opengis.annotation.UML;
import org.opengis.geometry.DirectPosition;
import org.opengis.util.CodeList;

/**
 * List of codes that may be used to identify the interpolation mechanisms. As a code list, there is
 * no intention of limiting the potential values of {@code CurveInterpolation}. Subtypes of {@link
 * CurveSegment} can be spawned directly through subclassing, or indirectly by specifying an
 * interpolation method and an associated control parameters record to support it.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as">ISO 19107</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 */
@UML(identifier = "GM_CurveInterpolation", specification = ISO_19107)
public final class CurveInterpolation extends CodeList<CurveInterpolation> {
    /** Serial number for compatibility with different versions. */
    private static final long serialVersionUID = 170309206092641598L;

    /** List of all enumerations of this type. Must be declared before any enum declaration. */
    private static final List<CurveInterpolation> VALUES = new ArrayList<>(10);

    /**
     * The interpolation mechanism shall return {@linkplain DirectPosition direct positions} on a
     * straight line between each consecutive pair of control points.
     */
    @UML(identifier = "linear", obligation = CONDITIONAL, specification = ISO_19107)
    public static final CurveInterpolation LINEAR = new CurveInterpolation("LINEAR");

    /**
     * The interpolation mechanism shall return {@linkplain DirectPosition direct positions} on a
     * geodesic curve between each consecutive pair of control points. A geodesic curve is a curve
     * of shortest length. The geodesic shall be determined in the coordinate reference system of
     * the {@linkplain Curve curve} in which the {@linkplain CurveSegment curve segment} is used.
     */
    @UML(identifier = "geodesic", obligation = CONDITIONAL, specification = ISO_19107)
    public static final CurveInterpolation GEODESIC = new CurveInterpolation("GEODESIC");

    /**
     * For each set of three consecutive control points, the middle one being an even offset from
     * the beginning of the sequence of control points, the interpolation mechanism shall return
     * {@linkplain DirectPosition direct positions} on a circular arc passing from the first point
     * through the middle point to the third point. The sequence of control points shall have an odd
     * number of elements. If the 3 points are co-linear, the circular arc becomes a straight line.
     */
    @UML(identifier = "circularArc3Points", obligation = CONDITIONAL, specification = ISO_19107)
    public static final CurveInterpolation CIRCULAR_ARC_3_POINTS =
            new CurveInterpolation("CIRCULAR_ARC_3_POINTS"); // Circular arc by 3 points

    /**
     * For each consecutive pair of control points, the interpolation mechanism shall return
     * {@linkplain DirectPosition direct positions} on a circular arc passing from the first control
     * point to the second control point, such that the associated control parameter determines the
     * offset of the center of the arc from the center point of the chord, positive for leftward and
     * negative for rightward. This form shall only be used in 2 dimensions because of the
     * restricted nature of the definition technique.
     */
    @UML(
            identifier = "circularArc2PointWithBulge",
            obligation = CONDITIONAL,
            specification = ISO_19107)
    public static final CurveInterpolation CIRCULAR_ARC_2_POINTS_WITH_BULGE =
            new CurveInterpolation(
                    "CIRCULAR_ARC_2_POINTS_WITH_BULGE"); // Circular arc by 2 points and bulge
    // factor

    /**
     * For each set of four consecutive control points, the interpolation mechanism shall return
     * {@linkplain DirectPosition direct positions} on an elliptical arc passing from the first
     * control point through the middle control points in order to the fourth control point. If the
     * 4 control points are co-linear, the arc becomes a straight line. If the 4 control points are
     * on the same circle, the arc becomes a circular one.
     */
    @UML(identifier = "elliptical", obligation = CONDITIONAL, specification = ISO_19107)
    public static final CurveInterpolation ELLIPTICAL =
            new CurveInterpolation("ELLIPTICAL"); // Elliptical arc

    /** Uses a Cornu's spiral or clothoid interpolation. */
    @UML(identifier = "clothoid", obligation = CONDITIONAL, specification = ISO_19107)
    public static final CurveInterpolation CLOTHOID = new CurveInterpolation("CLOTHOID");

    /**
     * Same as {@linkplain #ELLIPTICAL elliptical arc} but using 5 consecutive control points to
     * determine a conic section.
     */
    @UML(identifier = "conic", obligation = CONDITIONAL, specification = ISO_19107)
    public static final CurveInterpolation CONIC = new CurveInterpolation("CONIC"); // Conic arc
    /**
     * The control points are ordered as in a line-string, but they are spanned by a polynomial
     * spline function. Normally, the degree of continuity is determined by the degree of the
     * polynomials chosen.
     */
    @UML(identifier = "polynomialSpline", obligation = CONDITIONAL, specification = ISO_19107)
    public static final CurveInterpolation POLYNOMIAL_SPLINE =
            new CurveInterpolation("POLYNOMIAL_SPLINE"); // Polynomial Spline

    /**
     * The control points are interpolated using initial tangents and cubic polynomials, a form of
     * degree 3 polynomial spline.
     */
    @UML(identifier = "cubicSpline", obligation = CONDITIONAL, specification = ISO_19107)
    public static final CurveInterpolation CUBIC_SPLINE =
            new CurveInterpolation("CUBIC_SPLINE"); // Cubic Spline

    /**
     * The control points are ordered as in a line string, but they are spanned by a rational
     * (quotient of polynomials) spline function. Normally, the degree of continuity is determined
     * by the degree of the polynomials chosen.
     */
    @UML(identifier = "rationalSpline", obligation = CONDITIONAL, specification = ISO_19107)
    public static final CurveInterpolation RATIONAL_SPLINE =
            new CurveInterpolation("RATIONAL_SPLINE"); // Rational Spline

    /**
     * Constructs an enum with the given name. The new enum is automatically added to the list
     * returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     */
    private CurveInterpolation(final String name) {
        super(name, VALUES);
    }

    /**
     * Returns the list of {@code CurveInterpolation}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static CurveInterpolation[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new CurveInterpolation[VALUES.size()]);
        }
    }

    /** Returns the list of enumerations of the same kind than this enum. */
    @Override
    public CurveInterpolation[] family() {
        return values();
    }

    /**
     * Returns the curve interpolation that matches the given string, or returns a new one if none
     * match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static CurveInterpolation valueOf(String code) {
        return valueOf(CurveInterpolation.class, code);
    }
}
