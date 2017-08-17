/**
 */
package net.opengis.gml311;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Surface Interpolation Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * SurfaceInterpolationType is a list of codes that may be used to identify the interpolation mechanisms specified by an
 * application schema.
 * <!-- end-model-doc -->
 * @see net.opengis.gml311.Gml311Package#getSurfaceInterpolationType()
 * @model extendedMetaData="name='SurfaceInterpolationType'"
 * @generated
 */
public enum SurfaceInterpolationType implements Enumerator {
    /**
     * The '<em><b>None</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NONE_VALUE
     * @generated
     * @ordered
     */
    NONE(0, "none", "none"),

    /**
     * The '<em><b>Planar</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #PLANAR_VALUE
     * @generated
     * @ordered
     */
    PLANAR(1, "planar", "planar"),

    /**
     * The '<em><b>Spherical</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SPHERICAL_VALUE
     * @generated
     * @ordered
     */
    SPHERICAL(2, "spherical", "spherical"),

    /**
     * The '<em><b>Elliptical</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ELLIPTICAL_VALUE
     * @generated
     * @ordered
     */
    ELLIPTICAL(3, "elliptical", "elliptical"),

    /**
     * The '<em><b>Conic</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CONIC_VALUE
     * @generated
     * @ordered
     */
    CONIC(4, "conic", "conic"),

    /**
     * The '<em><b>Tin</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TIN_VALUE
     * @generated
     * @ordered
     */
    TIN(5, "tin", "tin"),

    /**
     * The '<em><b>Parametric Curve</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #PARAMETRIC_CURVE_VALUE
     * @generated
     * @ordered
     */
    PARAMETRIC_CURVE(6, "parametricCurve", "parametricCurve"),

    /**
     * The '<em><b>Polynomial Spline</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #POLYNOMIAL_SPLINE_VALUE
     * @generated
     * @ordered
     */
    POLYNOMIAL_SPLINE(7, "polynomialSpline", "polynomialSpline"),

    /**
     * The '<em><b>Rational Spline</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RATIONAL_SPLINE_VALUE
     * @generated
     * @ordered
     */
    RATIONAL_SPLINE(8, "rationalSpline", "rationalSpline"),

    /**
     * The '<em><b>Triangulated Spline</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TRIANGULATED_SPLINE_VALUE
     * @generated
     * @ordered
     */
    TRIANGULATED_SPLINE(9, "triangulatedSpline", "triangulatedSpline");

    /**
     * The '<em><b>None</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>None</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #NONE
     * @model name="none"
     * @generated
     * @ordered
     */
    public static final int NONE_VALUE = 0;

    /**
     * The '<em><b>Planar</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Planar</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #PLANAR
     * @model name="planar"
     * @generated
     * @ordered
     */
    public static final int PLANAR_VALUE = 1;

    /**
     * The '<em><b>Spherical</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Spherical</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #SPHERICAL
     * @model name="spherical"
     * @generated
     * @ordered
     */
    public static final int SPHERICAL_VALUE = 2;

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
    public static final int ELLIPTICAL_VALUE = 3;

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
    public static final int CONIC_VALUE = 4;

    /**
     * The '<em><b>Tin</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Tin</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #TIN
     * @model name="tin"
     * @generated
     * @ordered
     */
    public static final int TIN_VALUE = 5;

    /**
     * The '<em><b>Parametric Curve</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Parametric Curve</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #PARAMETRIC_CURVE
     * @model name="parametricCurve"
     * @generated
     * @ordered
     */
    public static final int PARAMETRIC_CURVE_VALUE = 6;

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
    public static final int POLYNOMIAL_SPLINE_VALUE = 7;

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
    public static final int RATIONAL_SPLINE_VALUE = 8;

    /**
     * The '<em><b>Triangulated Spline</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Triangulated Spline</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #TRIANGULATED_SPLINE
     * @model name="triangulatedSpline"
     * @generated
     * @ordered
     */
    public static final int TRIANGULATED_SPLINE_VALUE = 9;

    /**
     * An array of all the '<em><b>Surface Interpolation Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final SurfaceInterpolationType[] VALUES_ARRAY =
        new SurfaceInterpolationType[] {
            NONE,
            PLANAR,
            SPHERICAL,
            ELLIPTICAL,
            CONIC,
            TIN,
            PARAMETRIC_CURVE,
            POLYNOMIAL_SPLINE,
            RATIONAL_SPLINE,
            TRIANGULATED_SPLINE,
        };

    /**
     * A public read-only list of all the '<em><b>Surface Interpolation Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<SurfaceInterpolationType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Surface Interpolation Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SurfaceInterpolationType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SurfaceInterpolationType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Surface Interpolation Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SurfaceInterpolationType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SurfaceInterpolationType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Surface Interpolation Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SurfaceInterpolationType get(int value) {
        switch (value) {
            case NONE_VALUE: return NONE;
            case PLANAR_VALUE: return PLANAR;
            case SPHERICAL_VALUE: return SPHERICAL;
            case ELLIPTICAL_VALUE: return ELLIPTICAL;
            case CONIC_VALUE: return CONIC;
            case TIN_VALUE: return TIN;
            case PARAMETRIC_CURVE_VALUE: return PARAMETRIC_CURVE;
            case POLYNOMIAL_SPLINE_VALUE: return POLYNOMIAL_SPLINE;
            case RATIONAL_SPLINE_VALUE: return RATIONAL_SPLINE;
            case TRIANGULATED_SPLINE_VALUE: return TRIANGULATED_SPLINE;
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
    private SurfaceInterpolationType(int value, String name, String literal) {
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
    
} //SurfaceInterpolationType
