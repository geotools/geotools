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
 * A representation of the literals of the enumeration '<em><b>State Value Type Member0</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wfs20.Wfs20Package#getStateValueTypeMember0()
 * @model extendedMetaData="name='StateValueType_._member_._0'"
 * @generated
 */
public enum StateValueTypeMember0 implements Enumerator {
    /**
     * The '<em><b>Valid</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #VALID_VALUE
     * @generated
     * @ordered
     */
    VALID(0, "valid", "valid"),

    /**
     * The '<em><b>Superseded</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SUPERSEDED_VALUE
     * @generated
     * @ordered
     */
    SUPERSEDED(1, "superseded", "superseded"),

    /**
     * The '<em><b>Retired</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RETIRED_VALUE
     * @generated
     * @ordered
     */
    RETIRED(2, "retired", "retired"),

    /**
     * The '<em><b>Future</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FUTURE_VALUE
     * @generated
     * @ordered
     */
    FUTURE(3, "future", "future");

    /**
     * The '<em><b>Valid</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Valid</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #VALID
     * @model name="valid"
     * @generated
     * @ordered
     */
    public static final int VALID_VALUE = 0;

    /**
     * The '<em><b>Superseded</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Superseded</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #SUPERSEDED
     * @model name="superseded"
     * @generated
     * @ordered
     */
    public static final int SUPERSEDED_VALUE = 1;

    /**
     * The '<em><b>Retired</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Retired</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #RETIRED
     * @model name="retired"
     * @generated
     * @ordered
     */
    public static final int RETIRED_VALUE = 2;

    /**
     * The '<em><b>Future</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Future</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #FUTURE
     * @model name="future"
     * @generated
     * @ordered
     */
    public static final int FUTURE_VALUE = 3;

    /**
     * An array of all the '<em><b>State Value Type Member0</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final StateValueTypeMember0[] VALUES_ARRAY =
        new StateValueTypeMember0[] {
            VALID,
            SUPERSEDED,
            RETIRED,
            FUTURE,
        };

    /**
     * A public read-only list of all the '<em><b>State Value Type Member0</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<StateValueTypeMember0> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>State Value Type Member0</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static StateValueTypeMember0 get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            StateValueTypeMember0 result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>State Value Type Member0</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static StateValueTypeMember0 getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            StateValueTypeMember0 result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>State Value Type Member0</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static StateValueTypeMember0 get(int value) {
        switch (value) {
            case VALID_VALUE: return VALID;
            case SUPERSEDED_VALUE: return SUPERSEDED;
            case RETIRED_VALUE: return RETIRED;
            case FUTURE_VALUE: return FUTURE;
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
    private StateValueTypeMember0(int value, String name, String literal) {
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
    
} //StateValueTypeMember0
