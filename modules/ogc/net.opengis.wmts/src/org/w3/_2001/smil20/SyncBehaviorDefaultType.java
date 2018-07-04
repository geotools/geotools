/**
 */
package org.w3._2001.smil20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Sync Behavior Default Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.w3._2001.smil20.Smil20Package#getSyncBehaviorDefaultType()
 * @model extendedMetaData="name='syncBehaviorDefaultType'"
 * @generated
 */
public enum SyncBehaviorDefaultType implements Enumerator {
    /**
     * The '<em><b>Can Slip</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CAN_SLIP_VALUE
     * @generated
     * @ordered
     */
    CAN_SLIP(0, "canSlip", "canSlip"),

    /**
     * The '<em><b>Locked</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LOCKED_VALUE
     * @generated
     * @ordered
     */
    LOCKED(1, "locked", "locked"),

    /**
     * The '<em><b>Independent</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INDEPENDENT_VALUE
     * @generated
     * @ordered
     */
    INDEPENDENT(2, "independent", "independent"),

    /**
     * The '<em><b>Inherit</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #INHERIT_VALUE
     * @generated
     * @ordered
     */
    INHERIT(3, "inherit", "inherit");

    /**
     * The '<em><b>Can Slip</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Can Slip</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CAN_SLIP
     * @model name="canSlip"
     * @generated
     * @ordered
     */
    public static final int CAN_SLIP_VALUE = 0;

    /**
     * The '<em><b>Locked</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Locked</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #LOCKED
     * @model name="locked"
     * @generated
     * @ordered
     */
    public static final int LOCKED_VALUE = 1;

    /**
     * The '<em><b>Independent</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Independent</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #INDEPENDENT
     * @model name="independent"
     * @generated
     * @ordered
     */
    public static final int INDEPENDENT_VALUE = 2;

    /**
     * The '<em><b>Inherit</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Inherit</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #INHERIT
     * @model name="inherit"
     * @generated
     * @ordered
     */
    public static final int INHERIT_VALUE = 3;

    /**
     * An array of all the '<em><b>Sync Behavior Default Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final SyncBehaviorDefaultType[] VALUES_ARRAY =
        new SyncBehaviorDefaultType[] {
            CAN_SLIP,
            LOCKED,
            INDEPENDENT,
            INHERIT,
        };

    /**
     * A public read-only list of all the '<em><b>Sync Behavior Default Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<SyncBehaviorDefaultType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Sync Behavior Default Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SyncBehaviorDefaultType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SyncBehaviorDefaultType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Sync Behavior Default Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SyncBehaviorDefaultType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SyncBehaviorDefaultType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Sync Behavior Default Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SyncBehaviorDefaultType get(int value) {
        switch (value) {
            case CAN_SLIP_VALUE: return CAN_SLIP;
            case LOCKED_VALUE: return LOCKED;
            case INDEPENDENT_VALUE: return INDEPENDENT;
            case INHERIT_VALUE: return INHERIT;
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
    private SyncBehaviorDefaultType(int value, String name, String literal) {
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
    
} //SyncBehaviorDefaultType
