/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Non Negative Integer Or Unknown Member0</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wfs20.Wfs20Package#getNonNegativeIntegerOrUnknownMember0()
 * @model extendedMetaData="name='nonNegativeIntegerOrUnknown_._member_._0'"
 * @generated
 */
public enum NonNegativeIntegerOrUnknownMember0 implements Enumerator {
    /**
     * The '<em><b>Unknown</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #UNKNOWN_VALUE
     * @generated
     * @ordered
     */
    UNKNOWN(0, "unknown", "unknown");

    /**
     * The '<em><b>Unknown</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Unknown</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #UNKNOWN
     * @model name="unknown"
     * @generated
     * @ordered
     */
    public static final int UNKNOWN_VALUE = 0;

    /**
     * An array of all the '<em><b>Non Negative Integer Or Unknown Member0</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final NonNegativeIntegerOrUnknownMember0[] VALUES_ARRAY =
        new NonNegativeIntegerOrUnknownMember0[] {
            UNKNOWN,
        };

    /**
     * A public read-only list of all the '<em><b>Non Negative Integer Or Unknown Member0</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<NonNegativeIntegerOrUnknownMember0> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Non Negative Integer Or Unknown Member0</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static NonNegativeIntegerOrUnknownMember0 get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            NonNegativeIntegerOrUnknownMember0 result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Non Negative Integer Or Unknown Member0</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static NonNegativeIntegerOrUnknownMember0 getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            NonNegativeIntegerOrUnknownMember0 result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Non Negative Integer Or Unknown Member0</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static NonNegativeIntegerOrUnknownMember0 get(int value) {
        switch (value) {
            case UNKNOWN_VALUE: return UNKNOWN;
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
    private NonNegativeIntegerOrUnknownMember0(int value, String name, String literal) {
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
    
} //NonNegativeIntegerOrUnknownMember0
