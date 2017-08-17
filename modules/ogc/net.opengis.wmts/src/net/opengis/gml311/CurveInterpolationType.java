/**
 */
package net.opengis.gml311;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Curve Interpolation Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * CurveInterpolationType is a list of codes that may be used to identify the interpolation mechanisms specified by an
 * application schema.
 * <!-- end-model-doc -->
 * @see net.opengis.gml311.Gml311Package#getCurveInterpolationType()
 * @model extendedMetaData="name='CurveInterpolationType'"
 * @generated
 */
public enum CurveInterpolationType implements Enumerator {
    /**
     * The '<em><b>Linear</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LINEAR_VALUE
     * @generated
     * @ordered
     */
    LINEAR(0, "linear", "linear"),

    /**
     * The '<em><b>Geodesic</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GEODESIC_VALUE
     * @generated
     * @ordered
     */
    GEODESIC(1, "geodesic", "geodesic"),

    /**
     * The '<em><b>Circular Arc3 Points</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CIRCULAR_ARC3_POINTS_VALUE
     * @generated
     * @ordered
     */
    CIRCULAR_ARC3_POINTS(2, "circularArc3Points", "circularArc3Points"),

    /**
     * The '<em><b>Circular Arc2 Point With Bulge</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CIRCULAR_ARC2_POINT_WITH_BULGE_VALUE
     * @generated
     * @ordered
     */
    CIRCULAR_ARC2_POINT_WITH_BULGE(3, "circularArc2PointWithBulge", "circularArc2PointWithBulge"),

    /**
     * The '<em><b>Circular Arc Center Point With Radius</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CIRCULAR_ARC_CENTER_POINT_WITH_RADIUS_VALUE
     * @generated
     * @ordered
     */
    CIRCULAR_ARC_CENTER_POINT_WITH_RADIUS(4, "circularArcCenterPointWithRadius", "circularArcCenterPointWithRadius"),

    /**
     * The '<em><b>Elliptical</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ELLIPTICAL_VALUE
     * @generated
     * @ordered
     */
    ELLIPTICAL(5, "elliptical", "elliptical"),

    /**
     * The '<em><b>Clothoid</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CLOTHOID_VALUE
     * @generated
     * @ordered
     */
    CLOTHOID(6, "clothoid", "clothoid"),

    /**
     * The '<em><b>Conic</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CONIC_VALUE
     * @generated
     * @ordered
     */
    CONIC(7, "conic", "conic"),

    /**
     * The '<em><b>Polynomial Spline</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #POLYNOMIAL_SPLINE_VALUE
     * @generated
     * @ordered
     */
    POLYNOMIAL_SPLINE(8, "polynomialSpline", "polynomialSpline"),

    /**
     * The '<em><b>Cubic Spline</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CUBIC_SPLINE_VALUE
     * @generated
     * @ordered
     */
    CUBIC_SPLINE(9, "cubicSpline", "cubicSpline"),

    /**
     * The '<em><b>Rational Spline</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RATIONAL_SPLINE_VALUE
     * @generated
     * @ordered
     */
    RATIONAL_SPLINE(10, "rationalSpline", "rationalSpline");

    /**
     * The '<em><b>Linear</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Linear</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #LINEAR
     * @model name="linear"
     * @generated
     * @ordered
     */
    public static final int LINEAR_VALUE = 0;

    /**
     * The '<em><b>Geodesic</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Geodesic</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #GEODESIC
     * @model name="geodesic"
     * @generated
     * @ordered
     */
    public static final int GEODESIC_VALUE = 1;

    /**
     * The '<em><b>Circular Arc3 Points</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Circular Arc3 Points</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CIRCULAR_ARC3_POINTS
     * @model name="circularArc3Points"
     * @generated
     * @ordered
     */
    public static final int CIRCULAR_ARC3_POINTS_VALUE = 2;

    /**
     * The '<em><b>Circular Arc2 Point With Bulge</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Circular Arc2 Point With Bulge</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CIRCULAR_ARC2_POINT_WITH_BULGE
     * @model name="circularArc2PointWithBulge"
     * @generated
     * @ordered
     */
    public static final int CIRCULAR_ARC2_POINT_WITH_BULGE_VALUE = 3;

    /**
     * The '<em><b>Circular Arc Center Point With Radius</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Circular Arc Center Point With Radius</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CIRCULAR_ARC_CENTER_POINT_WITH_RADIUS
     * @model name="circularArcCenterPointWithRadius"
     * @generated
     * @ordered
     */
    public static final int CIRCULAR_ARC_CENTER_POINT_WITH_RADIUS_VALUE = 4;

    /**
     * The '<em><b>Elliptical</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Elliptical</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #ELLIPTICAL
     * @model name="elliptical"
     * @generated
     * @ordered
     */
    public static final int ELLIPTICAL_VALUE = 5;

    /**
     * The '<em><b>Clothoid</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Clothoid</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CLOTHOID
     * @model name="clothoid"
     * @generated
     * @ordered
     */
    public static final int CLOTHOID_VALUE = 6;

    /**
     * The '<em><b>Conic</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Conic</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CONIC
     * @model name="conic"
     * @generated
     * @ordered
     */
    public static final int CONIC_VALUE = 7;

    /**
     * The '<em><b>Polynomial Spline</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Polynomial Spline</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #POLYNOMIAL_SPLINE
     * @model name="polynomialSpline"
     * @generated
     * @ordered
     */
    public static final int POLYNOMIAL_SPLINE_VALUE = 8;

    /**
     * The '<em><b>Cubic Spline</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Cubic Spline</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CUBIC_SPLINE
     * @model name="cubicSpline"
     * @generated
     * @ordered
     */
    public static final int CUBIC_SPLINE_VALUE = 9;

    /**
     * The '<em><b>Rational Spline</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Rational Spline</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #RATIONAL_SPLINE
     * @model name="rationalSpline"
     * @generated
     * @ordered
     */
    public static final int RATIONAL_SPLINE_VALUE = 10;

    /**
     * An array of all the '<em><b>Curve Interpolation Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final CurveInterpolationType[] VALUES_ARRAY =
        new CurveInterpolationType[] {
            LINEAR,
            GEODESIC,
            CIRCULAR_ARC3_POINTS,
            CIRCULAR_ARC2_POINT_WITH_BULGE,
            CIRCULAR_ARC_CENTER_POINT_WITH_RADIUS,
            ELLIPTICAL,
            CLOTHOID,
            CONIC,
            POLYNOMIAL_SPLINE,
            CUBIC_SPLINE,
            RATIONAL_SPLINE,
        };

    /**
     * A public read-only list of all the '<em><b>Curve Interpolation Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<CurveInterpolationType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Curve Interpolation Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static CurveInterpolationType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            CurveInterpolationType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Curve Interpolation Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static CurveInterpolationType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            CurveInterpolationType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Curve Interpolation Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static CurveInterpolationType get(int value) {
        switch (value) {
            case LINEAR_VALUE: return LINEAR;
            case GEODESIC_VALUE: return GEODESIC;
            case CIRCULAR_ARC3_POINTS_VALUE: return CIRCULAR_ARC3_POINTS;
            case CIRCULAR_ARC2_POINT_WITH_BULGE_VALUE: return CIRCULAR_ARC2_POINT_WITH_BULGE;
            case CIRCULAR_ARC_CENTER_POINT_WITH_RADIUS_VALUE: return CIRCULAR_ARC_CENTER_POINT_WITH_RADIUS;
            case ELLIPTICAL_VALUE: return ELLIPTICAL;
            case CLOTHOID_VALUE: return CLOTHOID;
            case CONIC_VALUE: return CONIC;
            case POLYNOMIAL_SPLINE_VALUE: return POLYNOMIAL_SPLINE;
            case CUBIC_SPLINE_VALUE: return CUBIC_SPLINE;
            case RATIONAL_SPLINE_VALUE: return RATIONAL_SPLINE;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private CurveInterpolationType(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral() {
      return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }
    
} //CurveInterpolationType
