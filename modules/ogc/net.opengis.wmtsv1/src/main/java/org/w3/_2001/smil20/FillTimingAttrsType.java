/**
 */
package org.w3._2001.smil20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Fill Timing Attrs Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.w3._2001.smil20.Smil20Package#getFillTimingAttrsType()
 * @model extendedMetaData="name='fillTimingAttrsType'"
 * @generated
 */
public enum FillTimingAttrsType implements Enumerator {
    /**
     * The '<em><b>Remove</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #REMOVE_VALUE
     * @generated
     * @ordered
     */
    REMOVE(0, "remove", "remove"),

    /**
     * The '<em><b>Freeze</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #FREEZE_VALUE
     * @generated
     * @ordered
     */
    FREEZE(1, "freeze", "freeze"),

    /**
     * The '<em><b>Hold</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #HOLD_VALUE
     * @generated
     * @ordered
     */
    HOLD(2, "hold", "hold"),

    /**
     * The '<em><b>Auto</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #AUTO_VALUE
     * @generated
     * @ordered
     */
    AUTO(3, "auto", "auto"),

    /**
     * The '<em><b>Default</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DEFAULT_VALUE
     * @generated
     * @ordered
     */
    DEFAULT(4, "default", "default"),

    /**
     * The '<em><b>Transition</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TRANSITION_VALUE
     * @generated
     * @ordered
     */
    TRANSITION(5, "transition", "transition");

    /**
     * The '<em><b>Remove</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Remove</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #REMOVE
     * @model name="remove"
     * @generated
     * @ordered
     */
    public static final int REMOVE_VALUE = 0;

    /**
     * The '<em><b>Freeze</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Freeze</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #FREEZE
     * @model name="freeze"
     * @generated
     * @ordered
     */
    public static final int FREEZE_VALUE = 1;

    /**
     * The '<em><b>Hold</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Hold</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #HOLD
     * @model name="hold"
     * @generated
     * @ordered
     */
    public static final int HOLD_VALUE = 2;

    /**
     * The '<em><b>Auto</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Auto</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #AUTO
     * @model name="auto"
     * @generated
     * @ordered
     */
    public static final int AUTO_VALUE = 3;

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
    public static final int DEFAULT_VALUE = 4;

    /**
     * The '<em><b>Transition</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Transition</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #TRANSITION
     * @model name="transition"
     * @generated
     * @ordered
     */
    public static final int TRANSITION_VALUE = 5;

    /**
     * An array of all the '<em><b>Fill Timing Attrs Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final FillTimingAttrsType[] VALUES_ARRAY =
        new FillTimingAttrsType[] {
            REMOVE,
            FREEZE,
            HOLD,
            AUTO,
            DEFAULT,
            TRANSITION,
        };

    /**
     * A public read-only list of all the '<em><b>Fill Timing Attrs Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<FillTimingAttrsType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Fill Timing Attrs Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FillTimingAttrsType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FillTimingAttrsType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Fill Timing Attrs Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FillTimingAttrsType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FillTimingAttrsType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Fill Timing Attrs Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FillTimingAttrsType get(int value) {
        switch (value) {
            case REMOVE_VALUE: return REMOVE;
            case FREEZE_VALUE: return FREEZE;
            case HOLD_VALUE: return HOLD;
            case AUTO_VALUE: return AUTO;
            case DEFAULT_VALUE: return DEFAULT;
            case TRANSITION_VALUE: return TRANSITION;
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
    private FillTimingAttrsType(int value, String name, String literal) {
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
    
} //FillTimingAttrsType
