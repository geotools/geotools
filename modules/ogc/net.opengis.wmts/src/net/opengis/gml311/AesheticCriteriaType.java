/**
 */
package net.opengis.gml311;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Aeshetic Criteria Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Graph-specific styling property.
 * <!-- end-model-doc -->
 * @see net.opengis.gml311.Gml311Package#getAesheticCriteriaType()
 * @model extendedMetaData="name='AesheticCriteriaType'"
 * @generated
 */
public enum AesheticCriteriaType implements Enumerator {
    /**
     * The '<em><b>MINCROSSINGS</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MINCROSSINGS_VALUE
     * @generated
     * @ordered
     */
    MINCROSSINGS(0, "MINCROSSINGS", "MIN_CROSSINGS"),

    /**
     * The '<em><b>MINAREA</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MINAREA_VALUE
     * @generated
     * @ordered
     */
    MINAREA(1, "MINAREA", "MIN_AREA"),

    /**
     * The '<em><b>MINBENDS</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MINBENDS_VALUE
     * @generated
     * @ordered
     */
    MINBENDS(2, "MINBENDS", "MIN_BENDS"),

    /**
     * The '<em><b>MAXBENDS</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MAXBENDS_VALUE
     * @generated
     * @ordered
     */
    MAXBENDS(3, "MAXBENDS", "MAX_BENDS"),

    /**
     * The '<em><b>UNIFORMBENDS</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #UNIFORMBENDS_VALUE
     * @generated
     * @ordered
     */
    UNIFORMBENDS(4, "UNIFORMBENDS", "UNIFORM_BENDS"),

    /**
     * The '<em><b>MINSLOPES</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MINSLOPES_VALUE
     * @generated
     * @ordered
     */
    MINSLOPES(5, "MINSLOPES", "MIN_SLOPES"),

    /**
     * The '<em><b>MINEDGELENGTH</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MINEDGELENGTH_VALUE
     * @generated
     * @ordered
     */
    MINEDGELENGTH(6, "MINEDGELENGTH", "MIN_EDGE_LENGTH"),

    /**
     * The '<em><b>MAXEDGELENGTH</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MAXEDGELENGTH_VALUE
     * @generated
     * @ordered
     */
    MAXEDGELENGTH(7, "MAXEDGELENGTH", "MAX_EDGE_LENGTH"),

    /**
     * The '<em><b>UNIFORMEDGELENGTH</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #UNIFORMEDGELENGTH_VALUE
     * @generated
     * @ordered
     */
    UNIFORMEDGELENGTH(8, "UNIFORMEDGELENGTH", "UNIFORM_EDGE_LENGTH"),

    /**
     * The '<em><b>MAXANGULARRESOLUTION</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MAXANGULARRESOLUTION_VALUE
     * @generated
     * @ordered
     */
    MAXANGULARRESOLUTION(9, "MAXANGULARRESOLUTION", "MAX_ANGULAR_RESOLUTION"),

    /**
     * The '<em><b>MINASPECTRATIO</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MINASPECTRATIO_VALUE
     * @generated
     * @ordered
     */
    MINASPECTRATIO(10, "MINASPECTRATIO", "MIN_ASPECT_RATIO"),

    /**
     * The '<em><b>MAXSYMMETRIES</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MAXSYMMETRIES_VALUE
     * @generated
     * @ordered
     */
    MAXSYMMETRIES(11, "MAXSYMMETRIES", "MAX_SYMMETRIES");

    /**
     * The '<em><b>MINCROSSINGS</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MINCROSSINGS</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MINCROSSINGS
     * @model literal="MIN_CROSSINGS"
     * @generated
     * @ordered
     */
    public static final int MINCROSSINGS_VALUE = 0;

    /**
     * The '<em><b>MINAREA</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MINAREA</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MINAREA
     * @model literal="MIN_AREA"
     * @generated
     * @ordered
     */
    public static final int MINAREA_VALUE = 1;

    /**
     * The '<em><b>MINBENDS</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MINBENDS</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MINBENDS
     * @model literal="MIN_BENDS"
     * @generated
     * @ordered
     */
    public static final int MINBENDS_VALUE = 2;

    /**
     * The '<em><b>MAXBENDS</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MAXBENDS</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MAXBENDS
     * @model literal="MAX_BENDS"
     * @generated
     * @ordered
     */
    public static final int MAXBENDS_VALUE = 3;

    /**
     * The '<em><b>UNIFORMBENDS</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>UNIFORMBENDS</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #UNIFORMBENDS
     * @model literal="UNIFORM_BENDS"
     * @generated
     * @ordered
     */
    public static final int UNIFORMBENDS_VALUE = 4;

    /**
     * The '<em><b>MINSLOPES</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MINSLOPES</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MINSLOPES
     * @model literal="MIN_SLOPES"
     * @generated
     * @ordered
     */
    public static final int MINSLOPES_VALUE = 5;

    /**
     * The '<em><b>MINEDGELENGTH</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MINEDGELENGTH</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MINEDGELENGTH
     * @model literal="MIN_EDGE_LENGTH"
     * @generated
     * @ordered
     */
    public static final int MINEDGELENGTH_VALUE = 6;

    /**
     * The '<em><b>MAXEDGELENGTH</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MAXEDGELENGTH</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MAXEDGELENGTH
     * @model literal="MAX_EDGE_LENGTH"
     * @generated
     * @ordered
     */
    public static final int MAXEDGELENGTH_VALUE = 7;

    /**
     * The '<em><b>UNIFORMEDGELENGTH</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>UNIFORMEDGELENGTH</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #UNIFORMEDGELENGTH
     * @model literal="UNIFORM_EDGE_LENGTH"
     * @generated
     * @ordered
     */
    public static final int UNIFORMEDGELENGTH_VALUE = 8;

    /**
     * The '<em><b>MAXANGULARRESOLUTION</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MAXANGULARRESOLUTION</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MAXANGULARRESOLUTION
     * @model literal="MAX_ANGULAR_RESOLUTION"
     * @generated
     * @ordered
     */
    public static final int MAXANGULARRESOLUTION_VALUE = 9;

    /**
     * The '<em><b>MINASPECTRATIO</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MINASPECTRATIO</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MINASPECTRATIO
     * @model literal="MIN_ASPECT_RATIO"
     * @generated
     * @ordered
     */
    public static final int MINASPECTRATIO_VALUE = 10;

    /**
     * The '<em><b>MAXSYMMETRIES</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>MAXSYMMETRIES</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MAXSYMMETRIES
     * @model literal="MAX_SYMMETRIES"
     * @generated
     * @ordered
     */
    public static final int MAXSYMMETRIES_VALUE = 11;

    /**
     * An array of all the '<em><b>Aeshetic Criteria Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final AesheticCriteriaType[] VALUES_ARRAY =
        new AesheticCriteriaType[] {
            MINCROSSINGS,
            MINAREA,
            MINBENDS,
            MAXBENDS,
            UNIFORMBENDS,
            MINSLOPES,
            MINEDGELENGTH,
            MAXEDGELENGTH,
            UNIFORMEDGELENGTH,
            MAXANGULARRESOLUTION,
            MINASPECTRATIO,
            MAXSYMMETRIES,
        };

    /**
     * A public read-only list of all the '<em><b>Aeshetic Criteria Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<AesheticCriteriaType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Aeshetic Criteria Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static AesheticCriteriaType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            AesheticCriteriaType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Aeshetic Criteria Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static AesheticCriteriaType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            AesheticCriteriaType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Aeshetic Criteria Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static AesheticCriteriaType get(int value) {
        switch (value) {
            case MINCROSSINGS_VALUE: return MINCROSSINGS;
            case MINAREA_VALUE: return MINAREA;
            case MINBENDS_VALUE: return MINBENDS;
            case MAXBENDS_VALUE: return MAXBENDS;
            case UNIFORMBENDS_VALUE: return UNIFORMBENDS;
            case MINSLOPES_VALUE: return MINSLOPES;
            case MINEDGELENGTH_VALUE: return MINEDGELENGTH;
            case MAXEDGELENGTH_VALUE: return MAXEDGELENGTH;
            case UNIFORMEDGELENGTH_VALUE: return UNIFORMEDGELENGTH;
            case MAXANGULARRESOLUTION_VALUE: return MAXANGULARRESOLUTION;
            case MINASPECTRATIO_VALUE: return MINASPECTRATIO;
            case MAXSYMMETRIES_VALUE: return MAXSYMMETRIES;
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
    private AesheticCriteriaType(int value, String name, String literal) {
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
    
} //AesheticCriteriaType
