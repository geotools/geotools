/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Interpolation Method Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Codes that identify interpolation methods. The meanings of these codes are defined in Annex B of ISO 19123: Geographic information Ñ Schema for coverage geometry and functions.
 * <!-- end-model-doc -->
 * @see net.opengis.wcs10.Wcs10Package#getInterpolationMethodType()
 * @model extendedMetaData="name='InterpolationMethodType'"
 * @generated
 */
public final class InterpolationMethodType extends AbstractEnumerator {
    /**
	 * The '<em><b>Nearest Neighbor</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Nearest Neighbor</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #NEAREST_NEIGHBOR_LITERAL
	 * @model name="nearestNeighbor" literal="nearest neighbor"
	 * @generated
	 * @ordered
	 */
    public static final int NEAREST_NEIGHBOR = 0;

    /**
	 * The '<em><b>Bilinear</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Bilinear</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #BILINEAR_LITERAL
	 * @model name="bilinear"
	 * @generated
	 * @ordered
	 */
    public static final int BILINEAR = 1;

    /**
	 * The '<em><b>Bicubic</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Bicubic</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #BICUBIC_LITERAL
	 * @model name="bicubic"
	 * @generated
	 * @ordered
	 */
    public static final int BICUBIC = 2;

    /**
	 * The '<em><b>Lost Area</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Lost Area</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #LOST_AREA_LITERAL
	 * @model name="lostArea" literal="lost area"
	 * @generated
	 * @ordered
	 */
    public static final int LOST_AREA = 3;

    /**
	 * The '<em><b>Barycentric</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Barycentric</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @see #BARYCENTRIC_LITERAL
	 * @model name="barycentric"
	 * @generated
	 * @ordered
	 */
    public static final int BARYCENTRIC = 4;

    /**
	 * The '<em><b>None</b></em>' literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * No interpolation.
	 * <!-- end-model-doc -->
	 * @see #NONE_LITERAL
	 * @model name="none"
	 * @generated
	 * @ordered
	 */
    public static final int NONE = 5;

    /**
	 * The '<em><b>Nearest Neighbor</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #NEAREST_NEIGHBOR
	 * @generated
	 * @ordered
	 */
    public static final InterpolationMethodType NEAREST_NEIGHBOR_LITERAL = new InterpolationMethodType(NEAREST_NEIGHBOR, "nearestNeighbor", "nearest neighbor");

    /**
	 * The '<em><b>Bilinear</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #BILINEAR
	 * @generated
	 * @ordered
	 */
    public static final InterpolationMethodType BILINEAR_LITERAL = new InterpolationMethodType(BILINEAR, "bilinear", "bilinear");

    /**
	 * The '<em><b>Bicubic</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #BICUBIC
	 * @generated
	 * @ordered
	 */
    public static final InterpolationMethodType BICUBIC_LITERAL = new InterpolationMethodType(BICUBIC, "bicubic", "bicubic");

    /**
	 * The '<em><b>Lost Area</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #LOST_AREA
	 * @generated
	 * @ordered
	 */
    public static final InterpolationMethodType LOST_AREA_LITERAL = new InterpolationMethodType(LOST_AREA, "lostArea", "lost area");

    /**
	 * The '<em><b>Barycentric</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #BARYCENTRIC
	 * @generated
	 * @ordered
	 */
    public static final InterpolationMethodType BARYCENTRIC_LITERAL = new InterpolationMethodType(BARYCENTRIC, "barycentric", "barycentric");

    /**
	 * The '<em><b>None</b></em>' literal object.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #NONE
	 * @generated
	 * @ordered
	 */
    public static final InterpolationMethodType NONE_LITERAL = new InterpolationMethodType(NONE, "none", "none");

    /**
	 * An array of all the '<em><b>Interpolation Method Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private static final InterpolationMethodType[] VALUES_ARRAY =
        new InterpolationMethodType[] {
			NEAREST_NEIGHBOR_LITERAL,
			BILINEAR_LITERAL,
			BICUBIC_LITERAL,
			LOST_AREA_LITERAL,
			BARYCENTRIC_LITERAL,
			NONE_LITERAL,
		};

    /**
	 * A public read-only list of all the '<em><b>Interpolation Method Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
	 * Returns the '<em><b>Interpolation Method Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static InterpolationMethodType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InterpolationMethodType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Interpolation Method Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static InterpolationMethodType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InterpolationMethodType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

    /**
	 * Returns the '<em><b>Interpolation Method Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public static InterpolationMethodType get(int value) {
		switch (value) {
			case NEAREST_NEIGHBOR: return NEAREST_NEIGHBOR_LITERAL;
			case BILINEAR: return BILINEAR_LITERAL;
			case BICUBIC: return BICUBIC_LITERAL;
			case LOST_AREA: return LOST_AREA_LITERAL;
			case BARYCENTRIC: return BARYCENTRIC_LITERAL;
			case NONE: return NONE_LITERAL;
		}
		return null;
	}

    /**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    private InterpolationMethodType(int value, String name, String literal) {
		super(value, name, literal);
	}

} //InterpolationMethodType
