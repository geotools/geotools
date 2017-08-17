/**
 */
package net.opengis.gml311;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Knot Types Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Defines allowed values for the knots` type. Uniform knots implies that all knots are of multiplicity 1 and they differ by a positive constant from the preceding knot. Knots are quasi-uniform iff they are of multiplicity (degree + 1) at the ends, of multiplicity 1 elsewhere, and they differ by a positive constant from the preceding knot.
 * <!-- end-model-doc -->
 * @see net.opengis.gml311.Gml311Package#getKnotTypesType()
 * @model extendedMetaData="name='KnotTypesType'"
 * @generated
 */
public enum KnotTypesType implements Enumerator {
    /**
     * The '<em><b>Uniform</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #UNIFORM_VALUE
     * @generated
     * @ordered
     */
    UNIFORM(0, "uniform", "uniform"),

    /**
     * The '<em><b>Quasi Uniform</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #QUASI_UNIFORM_VALUE
     * @generated
     * @ordered
     */
    QUASI_UNIFORM(1, "quasiUniform", "quasiUniform"),

    /**
     * The '<em><b>Piecewise Bezier</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #PIECEWISE_BEZIER_VALUE
     * @generated
     * @ordered
     */
    PIECEWISE_BEZIER(2, "piecewiseBezier", "piecewiseBezier");

    /**
     * The '<em><b>Uniform</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Uniform</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #UNIFORM
     * @model name="uniform"
     * @generated
     * @ordered
     */
    public static final int UNIFORM_VALUE = 0;

    /**
     * The '<em><b>Quasi Uniform</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Quasi Uniform</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #QUASI_UNIFORM
     * @model name="quasiUniform"
     * @generated
     * @ordered
     */
    public static final int QUASI_UNIFORM_VALUE = 1;

    /**
     * The '<em><b>Piecewise Bezier</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Piecewise Bezier</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #PIECEWISE_BEZIER
     * @model name="piecewiseBezier"
     * @generated
     * @ordered
     */
    public static final int PIECEWISE_BEZIER_VALUE = 2;

    /**
     * An array of all the '<em><b>Knot Types Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final KnotTypesType[] VALUES_ARRAY =
        new KnotTypesType[] {
            UNIFORM,
            QUASI_UNIFORM,
            PIECEWISE_BEZIER,
        };

    /**
     * A public read-only list of all the '<em><b>Knot Types Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<KnotTypesType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Knot Types Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static KnotTypesType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            KnotTypesType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Knot Types Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static KnotTypesType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            KnotTypesType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Knot Types Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static KnotTypesType get(int value) {
        switch (value) {
            case UNIFORM_VALUE: return UNIFORM;
            case QUASI_UNIFORM_VALUE: return QUASI_UNIFORM;
            case PIECEWISE_BEZIER_VALUE: return PIECEWISE_BEZIER;
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
    private KnotTypesType(int value, String name, String literal) {
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
    
} //KnotTypesType
