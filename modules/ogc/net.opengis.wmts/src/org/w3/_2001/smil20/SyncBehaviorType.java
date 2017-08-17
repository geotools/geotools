/**
 */
package org.w3._2001.smil20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Sync Behavior Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.w3._2001.smil20.Smil20Package#getSyncBehaviorType()
 * @model extendedMetaData="name='syncBehaviorType'"
 * @generated
 */
public enum SyncBehaviorType implements Enumerator {
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
     * The '<em><b>Default</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DEFAULT_VALUE
     * @generated
     * @ordered
     */
    DEFAULT(3, "default", "default");

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
     * The '<em><b>Default</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Default</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #DEFAULT
     * @model name="default"
     * @generated
     * @ordered
     */
    public static final int DEFAULT_VALUE = 3;

    /**
     * An array of all the '<em><b>Sync Behavior Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final SyncBehaviorType[] VALUES_ARRAY =
        new SyncBehaviorType[] {
            CAN_SLIP,
            LOCKED,
            INDEPENDENT,
            DEFAULT,
        };

    /**
     * A public read-only list of all the '<em><b>Sync Behavior Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<SyncBehaviorType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Sync Behavior Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SyncBehaviorType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SyncBehaviorType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Sync Behavior Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SyncBehaviorType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            SyncBehaviorType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Sync Behavior Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static SyncBehaviorType get(int value) {
        switch (value) {
            case CAN_SLIP_VALUE: return CAN_SLIP;
            case LOCKED_VALUE: return LOCKED;
            case INDEPENDENT_VALUE: return INDEPENDENT;
            case DEFAULT_VALUE: return DEFAULT;
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
    private SyncBehaviorType(int value, String name, String literal) {
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
    
} //SyncBehaviorType
